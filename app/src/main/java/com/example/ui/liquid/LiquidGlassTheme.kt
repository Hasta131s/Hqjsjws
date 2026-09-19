package com.example.ui.liquid

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.model.IconShape

object LiquidGlassTheme {
    val DeepObsidian = Color(0xFF040A14)
    val GlassSurface = Color(0x18FFFFFF)
    val GlassSurfaceHighlight = Color(0x30FFFFFF)
    val GlassSurfaceSubtle = Color(0x10FFFFFF)

    val CyanGlow = Color(0xFF00F0FF)
    val ElectricAzure = Color(0xFF0077B6)
    val BioGreen = Color(0xFF00F5A0)
    val PureIce = Color(0xFFE2F3F5)
    val PrismaticViolet = Color(0xFF7209B7)

    // Border highlights simulating refractive glass edges
    val PrismaticBorderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0x80FFFFFF),
            Color(0x5000F0FF),
            Color(0x207209B7),
            Color(0x7000F0FF),
            Color(0x90FFFFFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val SubduedBorderBrush = Brush.linearGradient(
        colors = listOf(
            Color(0x45FFFFFF),
            Color(0x2000F0FF),
            Color(0x15FFFFFF)
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    fun glassBackdropBrush(opacity: Float = 0.16f): Brush {
        val alpha = (opacity * 255).toInt().coerceIn(10, 240)
        return Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFFFFF).copy(alpha = opacity * 1.4f),
                Color(0xFF0A2239).copy(alpha = opacity),
                Color(0xFF030D1A).copy(alpha = opacity * 1.8f)
            )
        )
    }

    fun getShapeForIcon(shape: IconShape): Shape {
        return when (shape) {
            IconShape.LIQUID_PEBBLE -> RoundedCornerShape(topStart = 24.dp, topEnd = 12.dp, bottomEnd = 24.dp, bottomStart = 16.dp)
            IconShape.DROPLET -> RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp, bottomEnd = 8.dp, bottomStart = 26.dp)
            IconShape.SQUIRCLE -> RoundedCornerShape(18.dp)
            IconShape.GLASS_ORB -> CircleShape
            IconShape.HEXA_AQUA -> CutCornerShape(12.dp)
        }
    }
}
