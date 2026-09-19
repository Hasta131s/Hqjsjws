package com.example.engine

import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import androidx.compose.ui.graphics.toArgb
import com.example.model.LiquidWallpaperConfig
import com.example.model.LiquidWallpaperType
import kotlin.math.cos
import kotlin.math.sin

/**
 * System-level Live Wallpaper Service providing the dynamic liquid-glass background
 * across the Android home screen and lock screen.
 */
class TvnahLiveWallpaperService : WallpaperService() {

    companion object {
        @Volatile
        var activeConfig: LiquidWallpaperConfig = LiquidWallpaperConfig.defaultFor(LiquidWallpaperType.HYDRA_ABYSS)
    }

    override fun onCreateEngine(): Engine {
        return LiquidGlassWallpaperEngine()
    }

    inner class LiquidGlassWallpaperEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var isVisible = false
        private var surfaceWidth = 1080
        private var surfaceHeight = 2400

        private var animTime = 0f
        private var touchX = -1f
        private var touchY = -1f
        private var touchAlpha = 0f

        private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val causticPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val specularPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val dropletPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        private val touchPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        private val frameRunnable = object : Runnable {
            override fun run() {
                if (isVisible) {
                    drawFrame()
                    handler.postDelayed(this, 16) // ~60 FPS
                }
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.isVisible = visible
            if (visible) {
                handler.post(frameRunnable)
            } else {
                handler.removeCallbacks(frameRunnable)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            surfaceWidth = width
            surfaceHeight = height
        }

        override fun onTouchEvent(event: MotionEvent) {
            super.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    touchX = event.x
                    touchY = event.y
                    touchAlpha = 1.0f
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // touchAlpha will fade out in draw loop
                }
            }
        }

        private fun drawFrame() {
            val holder = surfaceHolder ?: return
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    renderLiquidGlass(canvas)
                }
            } catch (e: Exception) {
                // Surface might be locked or destroyed
            } finally {
                if (canvas != null) {
                    try {
                        holder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }
        }

        private fun renderLiquidGlass(canvas: Canvas) {
            val config = activeConfig
            val type = config.wallpaperType
            val w = surfaceWidth.toFloat()
            val h = surfaceHeight.toFloat()
            val speed = config.flowSpeed.coerceIn(0.2f, 3.0f)
            animTime += 0.015f * speed

            val primaryArgb = type.primaryColor.toArgb()
            val secondaryArgb = type.secondaryColor.toArgb()
            val accentArgb = type.accentColor.toArgb()

            // 1. Vertical Base Fluid Gradient
            backgroundPaint.shader = LinearGradient(
                0f, 0f, 0f, h,
                intArrayOf(primaryArgb, secondaryArgb, AndroidColor.parseColor("#030710")),
                null,
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, w, h, backgroundPaint)

            // 2. Caustic Light Lenses
            val causticIntensity = config.causticIntensity
            val glassScale = config.glassThickness

            val cx1 = w * (0.35f + 0.16f * cos(animTime.toDouble()).toFloat())
            val cy1 = h * (0.30f + 0.12f * sin(animTime.toDouble()).toFloat())
            val r1 = w * 0.75f * glassScale

            val alpha1 = (55 * causticIntensity).toInt().coerceIn(5, 255)
            val accentColorWithAlpha = (accentArgb and 0x00FFFFFF) or (alpha1 shl 24)

            causticPaint.shader = RadialGradient(
                cx1, cy1, r1,
                intArrayOf(accentColorWithAlpha, (accentColorWithAlpha and 0x00FFFFFF), AndroidColor.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawCircle(cx1, cy1, r1, causticPaint)

            val cx2 = w * (0.72f - 0.15f * sin((animTime * 0.8).toDouble()).toFloat())
            val cy2 = h * (0.70f + 0.14f * cos((animTime * 0.8).toDouble()).toFloat())
            val r2 = w * 0.85f * glassScale
            val alpha2 = (80 * causticIntensity).toInt().coerceIn(5, 255)
            val secondaryColorWithAlpha = (secondaryArgb and 0x00FFFFFF) or (alpha2 shl 24)

            causticPaint.shader = RadialGradient(
                cx2, cy2, r2,
                intArrayOf(secondaryColorWithAlpha, (secondaryColorWithAlpha and 0x00FFFFFF), AndroidColor.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawCircle(cx2, cy2, r2, causticPaint)

            // 3. Specular Curved Ribbon
            val ribbonPhase = (animTime * 0.4f) % 1.5f - 0.25f
            val ribbonY = h * ribbonPhase
            val specAlpha = (22 * causticIntensity).toInt().coerceIn(0, 255)
            val specColor = AndroidColor.argb(specAlpha, 255, 255, 255)
            specPaint(specColor, ribbonY, w)
            canvas.drawRect(0f, ribbonY - 250f, w, ribbonY + 250f, specularPaint)

            // 4. Floating Droplets
            if (config.floatingDropletsEnabled) {
                val dropCount = config.dropletCount.coerceIn(2, 8)
                for (i in 0 until dropCount) {
                    val angle = i * (2 * Math.PI / dropCount)
                    val dropX = w * (0.25f + (i % 3) * 0.25f + 0.08f * cos(animTime.toDouble() * 1.2 + angle).toFloat())
                    val dropY = h * (0.28f + (i * 2 % 4) * 0.16f + 0.07f * sin(animTime.toDouble() * 1.1 + angle).toFloat())
                    val dropR = (22f + (i % 3) * 14f) * glassScale

                    val dropAlpha = (60 * causticIntensity).toInt().coerceIn(5, 255)
                    val dropAccent = (accentArgb and 0x00FFFFFF) or (dropAlpha shl 24)

                    dropletPaint.shader = RadialGradient(
                        dropX - dropR * 0.25f, dropY - dropR * 0.25f, dropR,
                        intArrayOf(dropAccent, secondaryColorWithAlpha, AndroidColor.TRANSPARENT),
                        floatArrayOf(0f, 0.7f, 1f),
                        Shader.TileMode.CLAMP
                    )
                    canvas.drawCircle(dropX, dropY, dropR, dropletPaint)

                    // Droplet rim
                    rimPaint.color = AndroidColor.argb((90 * causticIntensity).toInt(), 255, 255, 255)
                    canvas.drawCircle(dropX, dropY, dropR, rimPaint)

                    // Specular dot
                    dropletPaint.shader = null
                    dropletPaint.color = AndroidColor.argb((180 * causticIntensity).toInt(), 255, 255, 255)
                    canvas.drawCircle(dropX - dropR * 0.35f, dropY - dropR * 0.35f, dropR * 0.25f, dropletPaint)
                }
            }

            // 5. Interactive Touch Ripples
            if (touchAlpha > 0.02f && touchX >= 0) {
                val rippleAlpha = (70 * touchAlpha).toInt().coerceIn(0, 255)
                val rippleColor = (accentArgb and 0x00FFFFFF) or (rippleAlpha shl 24)
                val rippleR = w * 0.45f

                touchPaint.shader = RadialGradient(
                    touchX, touchY, rippleR,
                    intArrayOf(rippleColor, AndroidColor.TRANSPARENT),
                    floatArrayOf(0f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawCircle(touchX, touchY, rippleR, touchPaint)
                touchAlpha *= 0.94f // Decay ripple
            }
        }

        private fun specPaint(color: Int, ribbonY: Float, w: Float) {
            specularPaint.shader = LinearGradient(
                0f, ribbonY - 200f, w, ribbonY + 200f,
                intArrayOf(AndroidColor.TRANSPARENT, color, AndroidColor.TRANSPARENT),
                null,
                Shader.TileMode.CLAMP
            )
        }

        override fun onDestroy() {
            super.onDestroy()
            handler.removeCallbacks(frameRunnable)
        }
    }
}
