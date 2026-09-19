package com.example.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.EnergySavingsLeaf
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BatteryState
import com.example.model.PerformanceMode
import com.example.ui.liquid.LiquidGlassCard
import kotlin.math.sin

@Composable
fun HydraBatteryCapsuleWidget(
    batteryState: BatteryState,
    performanceMode: PerformanceMode,
    onToggleEcoMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCharging = batteryState.isCharging
    val level = batteryState.levelPercent
    val isEco = performanceMode == PerformanceMode.ECO_SAVER

    val liquidColor = when {
        isCharging -> Color(0xFF00F5A0)
        level > 50 -> Color(0xFF00F0FF)
        level > 20 -> Color(0xFFFFB703)
        else -> Color(0xFFFF4D6D)
    }

    val isAnimated = !isEco
    val transition = rememberInfiniteTransition(label = "LiquidSlosh")
    val waveOffset by if (isAnimated) {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2800, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "BatteryWave"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        blurRefractionAlpha = 0.18f,
        glowAccentColor = liquidColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(liquidColor.copy(alpha = 0.22f))
                    ) {
                        Icon(
                            imageVector = if (isCharging) Icons.Rounded.Bolt else Icons.Rounded.EnergySavingsLeaf,
                            contentDescription = "Battery",
                            tint = liquidColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isCharging) "Hızlı Sıvı Şarj" else "Hydra Pil Durumu",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "${batteryState.health} • ${(batteryState.voltageMv / 1000f)}V",
                            fontSize = 10.5.sp,
                            color = Color(0xFFB0BEC5)
                        )
                    }
                }

                // Eco mode pill toggle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isEco) Color(0xFF00F5A0).copy(alpha = 0.25f)
                            else Color.White.copy(alpha = 0.08f)
                        )
                        .border(
                            width = 1.dp,
                            color = if (isEco) Color(0xFF00F5A0).copy(alpha = 0.6f) else Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable(onClick = onToggleEcoMode)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isEco) "Eco: AKTİF" else "Eco: KAPALI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isEco) Color(0xFF00F5A0) else Color(0xFFE0E0E0)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Liquid Sloshing Capsule Gauge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0x25000000))
                    .border(1.dp, Color.White.copy(alpha = 0.22f), RoundedCornerShape(14.dp))
            ) {
                // Wave liquid canvas
                Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
                    val fillFraction = (level / 100f).coerceIn(0.02f, 1f)
                    val liquidWidth = size.width * fillFraction

                    val path = Path()
                    path.moveTo(0f, size.height)
                    path.lineTo(0f, 0f)

                    if (isAnimated && fillFraction < 0.99f) {
                        val step = 6f
                        var x = 0f
                        while (x <= liquidWidth) {
                            val waveY = 3.5f * sin((x / 40f + waveOffset)).toFloat()
                            path.lineTo(x, (size.height * 0.2f) + waveY)
                            x += step
                        }
                    } else {
                        path.lineTo(liquidWidth, 0f)
                    }

                    path.lineTo(liquidWidth, size.height)
                    path.close()

                    drawPath(
                        path = path,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                liquidColor.copy(alpha = 0.85f),
                                liquidColor,
                                liquidColor.copy(alpha = 0.95f)
                            )
                        )
                    )

                    // Specular light bar reflection on top of liquid
                    drawLine(
                        color = Color.White.copy(alpha = 0.45f),
                        start = Offset(4f, 4f),
                        end = Offset(liquidWidth - 4f, 4f),
                        strokeWidth = 2f
                    )
                }

                // Level text inside capsule
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .align(Alignment.CenterStart),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$level%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (isCharging) {
                        Text(
                            text = "Doluyor ⚡",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF040A14)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Thermal info & power stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Thermostat,
                        contentDescription = "Temperature",
                        tint = Color(0xFF00F0FF),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${batteryState.temperatureCelsius}°C Sıcaklık",
                        fontSize = 11.5.sp,
                        color = Color(0xFFE0F7FA)
                    )
                }

                Text(
                    text = if (isEco) "Pil Ömrü Koruma Motoru" else "${performanceMode.fpsLimit} FPS Sıvı Render",
                    fontSize = 11.sp,
                    color = Color(0xFF80DEEA),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
