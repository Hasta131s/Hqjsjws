package com.example.engine

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.toArgb
import com.example.model.LiquidWallpaperConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.cos
import kotlin.math.sin

/**
 * Target location for applying the liquid wallpaper on the Android OS.
 */
enum class SystemWallpaperTarget(val labelTr: String) {
    HOME_SCREEN("Ana Ekran"),
    LOCK_SCREEN("Kilit Ekranı"),
    BOTH("Ana Ekran & Kilit Ekranı")
}

/**
 * Manages applying the Liquid-Glass theme to the Android OS, including:
 * 1. Setting high-resolution static system wallpaper (Home/Lock screen)
 * 2. Launching Android Live Wallpaper service for animated dynamic liquid-glass
 * 3. Setting Tvnah Launcher as the Android system default launcher
 */
object SystemWallpaperManager {

    /**
     * Renders a high-resolution 1080x2400 (or device display size) bitmap with liquid-glass optics
     * and sets it as the Android OS system wallpaper.
     */
    suspend fun applyAsSystemWallpaper(
        context: Context,
        config: LiquidWallpaperConfig,
        target: SystemWallpaperTarget
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val wallpaperManager = WallpaperManager.getInstance(context)
            val displayMetrics = context.resources.displayMetrics
            val width = (displayMetrics.widthPixels).coerceAtLeast(1080)
            val height = (displayMetrics.heightPixels).coerceAtLeast(2160)

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            renderOpticsToCanvas(canvas, width.toFloat(), height.toFloat(), config)

            val whichFlag = when (target) {
                SystemWallpaperTarget.HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                SystemWallpaperTarget.LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                SystemWallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, whichFlag)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }

            bitmap.recycle()

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "✓ Sıvı Cam ${target.labelTr} için sistem duvar kağıdı yapıldı!",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Duvar kağıdı uygulanamadı: ${e.localizedMessage ?: "Bilinmeyen hata"}",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false
        }
    }

    /**
     * Launches the system Live Wallpaper chooser or directly the Tvnah Live Wallpaper preview.
     */
    fun launchLiveWallpaperPreview(context: Context, config: LiquidWallpaperConfig) {
        TvnahLiveWallpaperService.activeConfig = config
        try {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(context, TvnahLiveWallpaperService::class.java)
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback for devices where ACTION_CHANGE_LIVE_WALLPAPER is not supported
            try {
                val fallback = Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(fallback)
            } catch (ex: Exception) {
                Toast.makeText(
                    context,
                    "Canlı duvar kağıdı seçici açılamadı.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Prompts the user to set this launcher as the default Android home app.
     * Uses RoleManager for Android 10+ (Q+), Home settings intent, or chooser intent fallback.
     */
    fun openDefaultHomeSettings(context: Context) {
        val appContext = context.applicationContext ?: context

        // 1. Try RoleManager (Android 10+ / API 29+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                val roleManager = context.getSystemService(android.app.role.RoleManager::class.java)
                if (roleManager != null && roleManager.isRoleAvailable(android.app.role.RoleManager.ROLE_HOME)) {
                    if (!roleManager.isRoleHeld(android.app.role.RoleManager.ROLE_HOME)) {
                        val intent = roleManager.createRequestRoleIntent(android.app.role.RoleManager.ROLE_HOME).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                        return
                    } else {
                        Toast.makeText(context, "Zaten varsayılan başlatıcı olarak ayarlı.", Toast.LENGTH_SHORT).show()
                        return
                    }
                }
            } catch (e: Exception) {
                // RoleManager fallback
            }
        }

        // 2. Direct Settings ACTION_HOME_SETTINGS
        try {
            val intent = Intent(Settings.ACTION_HOME_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
            return
        } catch (e: Exception) {
            // Fallback
        }

        // 3. Fallback: ACTION_MANAGE_DEFAULT_APPS_SETTINGS
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
                return
            }
        } catch (e: Exception) {
            // Fallback
        }

        // 4. Fallback: Launch HOME chooser dialog via fake home intent
        try {
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val chooser = Intent.createChooser(homeIntent, "Varsayılan Başlatıcıyı Seç").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (ex: Exception) {
            try {
                val intent = Intent(Settings.ACTION_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Varsayılan uygulama ayarları açılamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renderOpticsToCanvas(
        canvas: Canvas,
        w: Float,
        h: Float,
        config: LiquidWallpaperConfig
    ) {
        val type = config.wallpaperType
        val primaryArgb = type.primaryColor.toArgb()
        val secondaryArgb = type.secondaryColor.toArgb()
        val accentArgb = type.accentColor.toArgb()

        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, h,
                intArrayOf(primaryArgb, secondaryArgb, AndroidColor.parseColor("#030710")),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, w, h, bgPaint)

        // Caustic blobs
        val causticPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val causticIntensity = config.causticIntensity
        val glassScale = config.glassThickness

        val cx1 = w * 0.38f
        val cy1 = h * 0.30f
        val r1 = w * 0.75f * glassScale
        val alpha1 = (70 * causticIntensity).toInt().coerceIn(10, 255)
        val accentColorWithAlpha = (accentArgb and 0x00FFFFFF) or (alpha1 shl 24)

        causticPaint.shader = RadialGradient(
            cx1, cy1, r1,
            intArrayOf(accentColorWithAlpha, (accentColorWithAlpha and 0x00FFFFFF), AndroidColor.TRANSPARENT),
            floatArrayOf(0f, 0.45f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawCircle(cx1, cy1, r1, causticPaint)

        val cx2 = w * 0.70f
        val cy2 = h * 0.68f
        val r2 = w * 0.85f * glassScale
        val alpha2 = (90 * causticIntensity).toInt().coerceIn(10, 255)
        val secondaryColorWithAlpha = (secondaryArgb and 0x00FFFFFF) or (alpha2 shl 24)

        causticPaint.shader = RadialGradient(
            cx2, cy2, r2,
            intArrayOf(secondaryColorWithAlpha, (secondaryColorWithAlpha and 0x00FFFFFF), AndroidColor.TRANSPARENT),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
        canvas.drawCircle(cx2, cy2, r2, causticPaint)

        // Specular Ribbon
        val specPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            val specAlpha = (24 * causticIntensity).toInt().coerceIn(0, 255)
            val specColor = AndroidColor.argb(specAlpha, 255, 255, 255)
            val ribbonY = h * 0.48f
            shader = LinearGradient(
                0f, ribbonY - 250f, w, ribbonY + 250f,
                intArrayOf(AndroidColor.TRANSPARENT, specColor, AndroidColor.TRANSPARENT),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, h * 0.48f - 250f, w, h * 0.48f + 250f, specPaint)

        // Floating Droplets
        if (config.floatingDropletsEnabled) {
            val dropPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 3f
                color = AndroidColor.argb((110 * causticIntensity).toInt(), 255, 255, 255)
            }

            val dropCount = config.dropletCount.coerceIn(2, 8)
            for (i in 0 until dropCount) {
                val angle = i * (2 * Math.PI / dropCount)
                val dropX = w * (0.22f + (i % 3) * 0.28f + 0.05f * cos(angle).toFloat())
                val dropY = h * (0.25f + ((i * 2) % 4) * 0.16f + 0.05f * sin(angle).toFloat())
                val dropR = (24f + (i % 3) * 16f) * glassScale

                val dropAlpha = (75 * causticIntensity).toInt().coerceIn(10, 255)
                val dropAccent = (accentArgb and 0x00FFFFFF) or (dropAlpha shl 24)

                dropPaint.shader = RadialGradient(
                    dropX - dropR * 0.25f, dropY - dropR * 0.25f, dropR,
                    intArrayOf(dropAccent, secondaryColorWithAlpha, AndroidColor.TRANSPARENT),
                    floatArrayOf(0f, 0.7f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawCircle(dropX, dropY, dropR, dropPaint)
                canvas.drawCircle(dropX, dropY, dropR, rimPaint)

                // Specular highlight glint
                dropPaint.shader = null
                dropPaint.color = AndroidColor.argb((200 * causticIntensity).toInt(), 255, 255, 255)
                canvas.drawCircle(dropX - dropR * 0.35f, dropY - dropR * 0.35f, dropR * 0.25f, dropPaint)
            }
        }
    }
}
