package com.example.ui.liquid

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.engine.MusicVisualizerManager
import com.example.model.LiquidWallpaperConfig
import com.example.model.LiquidWallpaperType
import com.example.model.PerformanceMode
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Data class representing an interactive ripple on the liquid glass surface.
 */
data class TouchLiquidRipple(
    val center: Offset,
    var currentRadius: Float,
    val maxRadius: Float,
    var alpha: Float
)

/**
 * High-performance, multi-layered Compose liquid-glass background renderer.
 * Features dynamic caustic waves, specular refraction ribbons, chromatic dispersion,
 * floating liquid-glass droplets, and interactive touch displacement.
 */
@Composable
fun LiquidWallpaper(
    config: LiquidWallpaperConfig,
    performanceMode: PerformanceMode,
    modifier: Modifier = Modifier,
    enableInteractiveTouch: Boolean = true,
    customImageUri: String? = null,
    isMusicReactive: Boolean = false
) {
    val rhythmPulse by MusicVisualizerManager.rhythmPulse.collectAsState()
    val activePulse = if (isMusicReactive) rhythmPulse else 1.0f

    val isAnimated = performanceMode != PerformanceMode.ECO_SAVER
    val speedMultiplier = (config.flowSpeed * (if (isMusicReactive) activePulse else 1.0f)).coerceIn(0.2f, 4.0f)
    val baseDuration1 = (14000L / speedMultiplier).toLong().coerceAtLeast(2000L)
    val baseDuration2 = (22000L / speedMultiplier).toLong().coerceAtLeast(3000L)
    val baseDuration3 = (18000L / speedMultiplier).toLong().coerceAtLeast(2500L)

    val infiniteTransition = rememberInfiniteTransition(label = "LiquidGlassOpticsTransition")

    val wavePhase1 by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = baseDuration1.toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "WavePhase1"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    val wavePhase2 by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = baseDuration2.toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "WavePhase2"
        )
    } else {
        remember { mutableStateOf(0f) }
    }

    val specularPhase by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = -0.3f,
            targetValue = 1.3f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = baseDuration3.toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "SpecularRibbonPhase"
        )
    } else {
        remember { mutableStateOf(0.4f) }
    }

    // Interactive touch perturbation point
    var touchPerturbation by remember { mutableStateOf(Offset.Zero) }
    var touchIntensity by remember { mutableStateOf(0f) }

    val interactiveModifier = if (enableInteractiveTouch && config.interactiveTouchReaction) {
        modifier.pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    touchPerturbation = offset
                    touchIntensity = 1.0f
                }
            )
        }.pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, _ ->
                    touchPerturbation = change.position
                    touchIntensity = 0.8f
                },
                onDragEnd = {
                    touchIntensity = 0f
                },
                onDragCancel = {
                    touchIntensity = 0f
                }
            )
        }
    } else {
        modifier
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (!customImageUri.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(customImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Custom Wallpaper",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Canvas(modifier = interactiveModifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val type = config.wallpaperType

            // 1. Base Liquid Gradient (Transparent if custom image is loaded)
            if (customImageUri.isNullOrEmpty()) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            type.primaryColor,
                            type.secondaryColor,
                            Color(0xFF030710)
                        )
                    )
                )
            } else {
                // Subtle darkening vignette over user's photo
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.45f),
                            Color.Black.copy(alpha = 0.70f)
                        )
                    )
                )
            }

            // Frosted Diffusion Layer
            if (config.frostedDiffusion > 0.02f) {
                drawRect(
                    color = Color.White.copy(alpha = config.frostedDiffusion * 0.06f)
                )
            }

            if (performanceMode.causticsEnabled) {
                val causticPower = config.causticIntensity * (if (isMusicReactive) (0.8f + activePulse * 0.5f) else 1.0f)
                val glassScale = config.glassThickness * (if (isMusicReactive) (0.92f + activePulse * 0.15f) else 1.0f)

            // 2. Multi-Harmonic Liquid Caustic Lenses
            // Primary liquid caustic blob
            val cx1 = w * (0.32f + 0.16f * cos(wavePhase1.toDouble()).toFloat())
            val cy1 = h * (0.28f + 0.12f * sin(wavePhase1.toDouble()).toFloat())
            drawLiquidCausticBlob(
                centerX = cx1,
                centerY = cy1,
                radius = w * 0.72f * glassScale,
                color = type.accentColor.copy(alpha = 0.22f * causticPower)
            )

            // Secondary liquid caustic blob
            val cx2 = w * (0.76f - 0.14f * sin(wavePhase2.toDouble()).toFloat())
            val cy2 = h * (0.68f + 0.15f * cos(wavePhase2.toDouble()).toFloat())
            drawLiquidCausticBlob(
                centerX = cx2,
                centerY = cy2,
                radius = w * 0.82f * glassScale,
                color = type.secondaryColor.copy(alpha = 0.38f * causticPower)
            )

            // Deep underwater luminescence blob
            val cx3 = w * (0.50f + 0.10f * sin((wavePhase1 * 1.6).toDouble()).toFloat())
            val cy3 = h * (0.88f - 0.08f * cos((wavePhase2 * 1.4).toDouble()).toFloat())
            drawLiquidCausticBlob(
                centerX = cx3,
                centerY = cy3,
                radius = w * 0.60f * glassScale,
                color = type.accentColor.copy(alpha = 0.16f * causticPower)
            )

            // Top ambient glass refraction highlight
            val cx4 = w * 0.5f
            val cy4 = -h * 0.1f
            drawLiquidCausticBlob(
                centerX = cx4,
                centerY = cy4,
                radius = w * 0.95f,
                color = type.accentColor.copy(alpha = 0.10f * causticPower)
            )

            // 3. Specular Liquid-Glass Curvature Ribbons
            val ribbonY = h * specularPhase
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.06f * causticPower),
                        type.accentColor.copy(alpha = 0.08f * causticPower),
                        Color.White.copy(alpha = 0.03f * causticPower),
                        Color.Transparent
                    ),
                    start = Offset(0f, ribbonY - 200f),
                    end = Offset(w, ribbonY + 200f)
                )
            )

            // 4. Prismatic Chromatic Dispersion Fringes (Glass Refraction Index n=1.52)
            if (config.chromaticAberration) {
                // Cyan dispersion rim
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x3000E5FF),
                            Color.Transparent
                        ),
                        center = Offset(cx1 - 18f, cy1 - 12f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(cx1 - 18f, cy1 - 12f)
                )

                // Magenta / violet dispersion rim
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x25BF55EC),
                            Color.Transparent
                        ),
                        center = Offset(cx1 + 18f, cy1 + 12f),
                        radius = w * 0.55f
                    ),
                    radius = w * 0.55f,
                    center = Offset(cx1 + 18f, cy1 + 12f)
                )
            }

            // 5. Floating Liquid Glass Droplets / Metaballs
            if (config.floatingDropletsEnabled && isAnimated) {
                val dropletCount = config.dropletCount.coerceIn(2, 10)
                for (i in 0 until dropletCount) {
                    val angleOffset = i * (2 * Math.PI / dropletCount)
                    val speedFactor = 0.7f + (i * 0.25f)
                    val orbitX = 0.2f + (i % 3) * 0.3f
                    val orbitY = 0.25f + ((i * 2) % 4) * 0.18f

                    val dropX = w * (orbitX + 0.10f * cos(wavePhase1.toDouble() * speedFactor + angleOffset).toFloat())
                    val dropY = h * (orbitY + 0.08f * sin(wavePhase2.toDouble() * speedFactor + angleOffset).toFloat())
                    val dropRadius = (16f + (i % 4) * 10f) * glassScale

                    drawLiquidGlassDroplet(
                        center = Offset(dropX, dropY),
                        radius = dropRadius,
                        accentColor = type.accentColor,
                        secondaryColor = type.secondaryColor,
                        causticPower = causticPower
                    )
                }
            }

            // 6. Interactive Touch Fluid Perturbation
            if (touchIntensity > 0.05f && touchPerturbation != Offset.Zero) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            type.accentColor.copy(alpha = 0.35f * touchIntensity),
                            Color.White.copy(alpha = 0.15f * touchIntensity),
                            Color.Transparent
                        ),
                        center = touchPerturbation,
                        radius = w * 0.45f
                    ),
                    radius = w * 0.45f,
                    center = touchPerturbation
                )
            }
        }
    }
}
}

/**
 * Renders an organic liquid caustic refraction blob.
 */
private fun DrawScope.drawLiquidCausticBlob(
    centerX: Float,
    centerY: Float,
    radius: Float,
    color: Color
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                color,
                color.copy(alpha = color.alpha * 0.45f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius
        ),
        radius = radius,
        center = Offset(centerX, centerY)
    )
}

/**
 * Renders a floating 3D liquid glass droplet with inner refraction core and specular highlight.
 */
private fun DrawScope.drawLiquidGlassDroplet(
    center: Offset,
    radius: Float,
    accentColor: Color,
    secondaryColor: Color,
    causticPower: Float
) {
    // 1. Ambient Glass Aura
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                accentColor.copy(alpha = 0.18f * causticPower),
                Color.Transparent
            ),
            center = center,
            radius = radius * 1.8f
        ),
        radius = radius * 1.8f,
        center = center
    )

    // 2. Liquid Droplet Body (Refractive Core)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                accentColor.copy(alpha = 0.28f * causticPower),
                secondaryColor.copy(alpha = 0.20f * causticPower),
                Color.White.copy(alpha = 0.12f)
            ),
            center = Offset(center.x - radius * 0.25f, center.y - radius * 0.25f),
            radius = radius
        ),
        radius = radius,
        center = center
    )

    // 3. Liquid Glass Rim
    drawCircle(
        color = Color.White.copy(alpha = 0.35f * causticPower),
        radius = radius,
        center = center,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.4f)
    )

    // 4. Top-Left Specular Light Glint
    drawCircle(
        color = Color.White.copy(alpha = 0.75f * causticPower),
        radius = radius * 0.25f,
        center = Offset(center.x - radius * 0.38f, center.y - radius * 0.38f)
    )
}
