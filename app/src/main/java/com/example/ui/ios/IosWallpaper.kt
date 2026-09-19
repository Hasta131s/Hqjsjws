package com.example.ui.ios

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.engine.MusicVisualizerManager
import com.example.model.IosWallpaperPreset
import kotlin.math.sin

/**
 * Authentic Apple iOS 18 & 17 Wallpaper Engine.
 * Features elegant Apple silk ribbons, ambient atmospheric depth, and vibrant OLED contrasts.
 * Completely free of water ripples or liquid caustics.
 */
@Composable
fun IosWallpaper(
    preset: IosWallpaperPreset,
    modifier: Modifier = Modifier,
    customImageUri: String? = null,
    isMusicReactive: Boolean = false
) {
    val rhythmPulse by MusicVisualizerManager.rhythmPulse.collectAsState()
    val activePulse = if (isMusicReactive) rhythmPulse else 1.0f

    val infiniteTransition = rememberInfiniteTransition(label = "IosSilkMotion")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "SilkRibbonWave"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // If user picked a custom photo or online wallpaper, render it
        if (!customImageUri.isNullOrEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(customImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Özel Duvar Kağıdı",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Subtle iOS dark gradient for top status bar and bottom dock readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            // Official Apple iOS Silk & Atmospheric Gradient Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // 1. Base Apple Inset Gradient
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            preset.topColor,
                            preset.midColor,
                            preset.bottomColor
                        )
                    )
                )

                // 2. Apple Silk Glow Arc 1 (Upper vibrant ribbon)
                val ribbonPulse = if (isMusicReactive) activePulse * 1.15f else 1.0f
                val arc1CenterY = h * 0.35f + 40f * sin(waveOffset.toDouble()).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            preset.accentColor.copy(alpha = 0.28f * ribbonPulse),
                            preset.accentColor.copy(alpha = 0.10f * ribbonPulse),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.75f, arc1CenterY),
                        radius = w * 0.95f
                    ),
                    radius = w * 0.95f,
                    center = Offset(w * 0.75f, arc1CenterY)
                )

                // 3. Apple Silk Glow Arc 2 (Lower harmonic glow)
                val arc2CenterY = h * 0.72f - 30f * sin(waveOffset.toDouble() + 1.2).toFloat()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            preset.midColor.copy(alpha = 0.45f * ribbonPulse),
                            preset.accentColor.copy(alpha = 0.18f * ribbonPulse),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.20f, arc2CenterY),
                        radius = w * 0.90f
                    ),
                    radius = w * 0.90f,
                    center = Offset(w * 0.20f, arc2CenterY)
                )

                // 4. Subtle Apple Silk Ribbon Wave Path (S-Curve)
                val path = Path().apply {
                    moveTo(0f, h * 0.28f)
                    cubicTo(
                        w * 0.40f, h * (0.24f + 0.04f * sin(waveOffset.toDouble()).toFloat()),
                        w * 0.65f, h * (0.52f + 0.03f * sin(waveOffset.toDouble() + 1.0).toFloat()),
                        w, h * 0.48f
                    )
                    lineTo(w, h * 0.60f)
                    cubicTo(
                        w * 0.65f, h * (0.64f + 0.03f * sin(waveOffset.toDouble() + 1.0).toFloat()),
                        w * 0.40f, h * (0.36f + 0.04f * sin(waveOffset.toDouble()).toFloat()),
                        0f, h * 0.40f
                    )
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            preset.accentColor.copy(alpha = 0.16f * ribbonPulse),
                            preset.accentColor.copy(alpha = 0.08f * ribbonPulse),
                            Color.Transparent
                        ),
                        start = Offset(0f, h * 0.3f),
                        end = Offset(w, h * 0.55f)
                    )
                )

                // 5. iOS System Dark Vignette at the very top and bottom for dock/header clarity
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.45f)
                        ),
                        startY = 0f,
                        endY = h
                    )
                )
            }
        }
    }
}
