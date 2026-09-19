package com.example.ui.liquid

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    borderBrush: Brush = LiquidGlassTheme.PrismaticBorderBrush,
    borderWidth: Dp = 1.dp,
    blurRefractionAlpha: Float = 0.18f,
    glowAccentColor: Color? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "LiquidCardScale"
    )

    Box(
        modifier = modifier
            .scale(pressScale)
            .clip(shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                } else Modifier
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF).copy(alpha = blurRefractionAlpha * 1.5f),
                        Color(0xFF102842).copy(alpha = blurRefractionAlpha * 0.9f),
                        Color(0xFF040E1B).copy(alpha = blurRefractionAlpha * 1.6f)
                    )
                )
            )
            .border(
                BorderStroke(borderWidth, borderBrush),
                shape = shape
            )
            .drawBehind {
                // Specular lens reflection along top curvature (simulates liquid meniscus)
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.White.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.28f
                    ),
                    size = Size(size.width, size.height * 0.28f),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx())
                )

                // Optional subtle glow refraction
                if (glowAccentColor != null) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                glowAccentColor.copy(alpha = 0.22f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.85f, size.height * 0.2f),
                            radius = size.width * 0.6f
                        ),
                        center = Offset(size.width * 0.85f, size.height * 0.2f),
                        radius = size.width * 0.6f
                    )
                }
            }
            .padding(1.dp),
        content = content
    )
}
