package com.example.ui.widgets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PerformanceMode
import com.example.ui.liquid.LiquidGlassCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sin

@Composable
fun LiquidClockWidget(
    performanceMode: PerformanceMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000L)
        }
    }

    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val secondsFormat = remember { SimpleDateFormat("ss", Locale.getDefault()) }
    val dateFormat = remember { SimpleDateFormat("EEEE, d MMMM", Locale("tr", "TR")) }

    val isAnimated = performanceMode != PerformanceMode.ECO_SAVER
    val infiniteTransition = rememberInfiniteTransition(label = "ClockWaterWave")
    val wavePhase by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (2 * Math.PI).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "WavePhase"
        )
    } else {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    LiquidGlassCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        blurRefractionAlpha = 0.20f,
        glowAccentColor = Color(0xFF00F0FF),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Undulating water wave along the bottom of the clock card
            if (isAnimated) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    val path = Path()
                    val waveHeight = 9f
                    val baseHeight = size.height * 0.55f

                    path.moveTo(0f, size.height)
                    path.lineTo(0f, baseHeight)

                    val step = 10f
                    var x = 0f
                    while (x <= size.width + step) {
                        val y = baseHeight + waveHeight * sin((x / size.width * 2 * Math.PI + wavePhase)).toFloat()
                        path.lineTo(x, y)
                        x += step
                    }

                    path.lineTo(size.width, size.height)
                    path.close()

                    drawPath(
                        path = path,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0x3500F0FF),
                                Color(0x100077B6)
                            )
                        )
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.WaterDrop,
                            contentDescription = "Tvnah Liquid Flow",
                            tint = Color(0xFF00F0FF),
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TVNAH AERO-HYDRA",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp,
                            color = Color(0xFF80DEEA)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Alarm,
                            contentDescription = "Alarm",
                            tint = Color(0xFFB0BEC5),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "07:30",
                            fontSize = 11.5.sp,
                            color = Color(0xFFCFD8DC),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = timeFormat.format(currentTime),
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraLight,
                        letterSpacing = (-1.5).sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = secondsFormat.format(currentTime),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF00F0FF),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = dateFormat.format(currentTime).replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE0F7FA).copy(alpha = 0.85f)
                )
            }
        }
    }
}
