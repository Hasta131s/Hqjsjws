package com.example.ui.liquid

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

data class ActiveRipple(
    val id: Long,
    val center: Offset,
    val progress: Animatable<Float, *>,
    val color: Color
)

@Composable
fun WaterRippleContainer(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    rippleColor: Color = Color(0xFF00F0FF),
    onTap: ((Offset) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val ripples = remember { mutableStateListOf<ActiveRipple>() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier.pointerInput(enabled) {
            if (!enabled) return@pointerInput
            detectTapGestures { offset ->
                val id = System.currentTimeMillis()
                val animatable = Animatable(0f)
                val ripple = ActiveRipple(id, offset, animatable, rippleColor)
                ripples.add(ripple)
                scope.launch {
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 650, easing = LinearOutSlowInEasing)
                    )
                    ripples.remove(ripple)
                }
                onTap?.invoke(offset)
            }
        }
    ) {
        content()

        if (enabled && ripples.isNotEmpty()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                ripples.forEach { ripple ->
                    val p = ripple.progress.value
                    val maxRadius = size.width.coerceAtLeast(size.height) * 0.45f
                    val currentRadius = p * maxRadius
                    val alpha = (1f - p).coerceIn(0f, 1f)

                    // Primary liquid refraction wavefront
                    drawCircle(
                        color = ripple.color.copy(alpha = alpha * 0.45f),
                        radius = currentRadius,
                        center = ripple.center,
                        style = Stroke(width = (4f * (1f - p)).coerceAtLeast(1f))
                    )

                    // Secondary trailing specular wave
                    if (currentRadius > 15f) {
                        drawCircle(
                            color = Color.White.copy(alpha = alpha * 0.6f),
                            radius = currentRadius * 0.72f,
                            center = ripple.center,
                            style = Stroke(width = (2f * (1f - p)).coerceAtLeast(0.5f))
                        )
                    }

                    // Inner soft caustic glow
                    drawCircle(
                        color = ripple.color.copy(alpha = alpha * 0.15f),
                        radius = currentRadius * 0.45f,
                        center = ripple.center
                    )
                }
            }
        }
    }
}
