package com.example.ui.liquid

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.model.LiquidWallpaperType
import com.example.model.PerformanceMode
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LiquidWallpaper(
    wallpaperType: LiquidWallpaperType,
    performanceMode: PerformanceMode,
    modifier: Modifier = Modifier
) {
    val isAnimated = performanceMode != PerformanceMode.ECO_SAVER

    val infiniteTransition = rememberInfiniteTransition(label = "LiquidCausticWave")
    
    val waveOffset by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 14000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "WavePhase"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    val secondaryOffset by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 20000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "SecondaryPhase"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        // Base deep aquatic gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    wallpaperType.primaryColor,
                    wallpaperType.secondaryColor,
                    Color(0xFF02060D)
                )
            )
        )

        if (performanceMode.causticsEnabled) {
            // Draw floating organic liquid caustics (simulated water refraction lenses)
            drawLiquidCausticBlob(
                centerX = size.width * (0.3f + 0.15f * cos(waveOffset.toDouble()).toFloat()),
                centerY = size.height * (0.25f + 0.1f * sin(waveOffset.toDouble()).toFloat()),
                radius = size.width * 0.65f,
                color = wallpaperType.accentColor.copy(alpha = 0.16f)
            )

            drawLiquidCausticBlob(
                centerX = size.width * (0.75f - 0.12f * sin(secondaryOffset.toDouble()).toFloat()),
                centerY = size.height * (0.65f + 0.14f * cos(secondaryOffset.toDouble()).toFloat()),
                radius = size.width * 0.75f,
                color = wallpaperType.secondaryColor.copy(alpha = 0.35f)
            )

            drawLiquidCausticBlob(
                centerX = size.width * 0.5f,
                centerY = size.height * (0.85f + 0.08f * sin(waveOffset.toDouble() * 1.5).toFloat()),
                radius = size.width * 0.55f,
                color = wallpaperType.accentColor.copy(alpha = 0.12f)
            )

            // Specular surface water sheen
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.04f),
                        Color.Transparent,
                        wallpaperType.accentColor.copy(alpha = 0.03f),
                        Color.Transparent
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height * 0.7f)
                )
            )
        }
    }
}

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
                color.copy(alpha = color.alpha * 0.4f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius
        ),
        radius = radius,
        center = Offset(centerX, centerY)
    )
}
