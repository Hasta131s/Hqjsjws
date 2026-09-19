package com.example.ui.ios

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.FlashlightOff
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BatteryState
import com.example.model.WeatherState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern Minimalist Lockscreen & SpringBoard Clock & Date Header.
 * Fully customizable size and font weight.
 */
@Composable
fun IosClockHeader(
    fontFamily: FontFamily,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSizeSp: Float = 68f,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var currentTime by remember { mutableStateOf("") }
    var currentDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateFormat = SimpleDateFormat("d MMMM EEEE", Locale("tr"))
        while (true) {
            val now = Date()
            currentTime = timeFormat.format(now)
            currentDate = dateFormat.format(now)
            delay(1000)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 8.dp, bottom = 4.dp)
    ) {
        // Date Header (e.g. 19 Eylül Cumartesi)
        Text(
            text = currentDate.ifEmpty { "Bugün" },
            fontSize = (fontSizeSp * 0.22f).coerceIn(13f, 22f).sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
            color = Color.White.copy(alpha = 0.90f),
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.85f),
                    offset = Offset(0f, 2f),
                    blurRadius = 4f
                )
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Customizable Big Clock (e.g. 14:52)
        Text(
            text = currentTime.ifEmpty { "12:00" },
            fontSize = fontSizeSp.sp,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            letterSpacing = (-1.0).sp,
            color = Color.White,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.85f),
                    offset = Offset(0f, 3f),
                    blurRadius = 6f
                )
            )
        )
    }
}

/**
 * 2x2 Weather Widget Card (Modern Matte Surface, customizable scale).
 */
@Composable
fun IosWeatherWidget(
    weatherState: WeatherState,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    scale: Float = 1.0f,
    onClick: () -> Unit = {}
) {
    val widgetShape = RoundedCornerShape(22.dp)
    val widthDp = (155 * scale).dp
    val heightDp = (148 * scale).dp

    Box(
        modifier = modifier
            .size(width = widthDp, height = heightDp)
            .shadow(8.dp, widgetShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(widgetShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), widgetShape)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = weatherState.city,
                        fontSize = (13.5f * scale).sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${weatherState.temperatureCelsius}°",
                        fontSize = (30f * scale).sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Icon(
                    imageVector = Icons.Rounded.WbSunny,
                    contentDescription = "Hava Durumu",
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size((26 * scale).dp)
                )
            }

            Column {
                Text(
                    text = weatherState.condition,
                    fontSize = (11f * scale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Y:${weatherState.temperatureCelsius + 3}° D:${weatherState.temperatureCelsius - 4}°",
                    fontSize = (10.5f * scale).sp,
                    fontFamily = fontFamily,
                    color = Color.White.copy(alpha = 0.60f)
                )
            }
        }
    }
}

/**
 * 2x2 Battery Widget Card (Modern Matte Surface, customizable scale).
 */
@Composable
fun IosBatteryWidget(
    batteryState: BatteryState,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    scale: Float = 1.0f,
    onClick: () -> Unit = {}
) {
    val widgetShape = RoundedCornerShape(22.dp)
    val widthDp = (155 * scale).dp
    val heightDp = (148 * scale).dp

    Box(
        modifier = modifier
            .size(width = widthDp, height = heightDp)
            .shadow(8.dp, widgetShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(widgetShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), widgetShape)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pil Durumu",
                    fontSize = (13f * scale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Icon(
                    imageVector = Icons.Rounded.BatteryFull,
                    contentDescription = "Pil",
                    tint = Color(0xFF34C759),
                    modifier = Modifier.size((18 * scale).dp)
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                // Background Track
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size((50 * scale).dp),
                    color = Color.White.copy(alpha = 0.12f),
                    strokeWidth = 5.dp
                )
                // Active Level
                val batteryColor = if (batteryState.levelPercent > 20) Color(0xFF34C759) else Color(0xFFFF9500)
                CircularProgressIndicator(
                    progress = { batteryState.levelPercent / 100f },
                    modifier = Modifier.size((50 * scale).dp),
                    color = batteryColor,
                    strokeWidth = 5.dp
                )
                Text(
                    text = "${batteryState.levelPercent}%",
                    fontSize = (12f * scale).sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = if (batteryState.isCharging) "⚡ Şarj Ediliyor" else "Cihaz Pili",
                fontSize = (11f * scale).sp,
                fontFamily = fontFamily,
                color = Color.White.copy(alpha = 0.65f)
            )
        }
    }
}

/**
 * Modern Search Pill.
 */
@Composable
fun IosSearchPill(
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Ara",
                tint = Color.White.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Ara",
                fontSize = 12.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.White.copy(alpha = 0.90f)
            )
        }
    }
}

/**
 * Flashlight Quick Action button (Solid matte circle, glowing amber when on).
 */
@Composable
fun IosQuickActionButton(
    isTorch: Boolean,
    isTorchOn: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(targetValue = if (isTorch && isTorchOn) 1.08f else 1.0f, label = "btn_scale")

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(48.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (isTorch && isTorchOn) Color(0xFFFFD60A)
                else Color(0xFF1E2024)
            )
            .border(
                0.5.dp,
                if (isTorch && isTorchOn) Color(0xFFFFE57F) else Color.White.copy(alpha = 0.20f),
                CircleShape
            )
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = if (isTorchOn) Icons.Rounded.FlashlightOn else Icons.Rounded.FlashlightOff,
            contentDescription = "Fener",
            tint = if (isTorchOn) Color.Black else Color.White,
            modifier = Modifier.size(22.dp)
        )
    }
}
