package com.example.ui.ios

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.BrightnessMedium
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.FastForward
import androidx.compose.material.icons.rounded.FastRewind
import androidx.compose.material.icons.rounded.FlashlightOff
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Headphones
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.NotificationsOff
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material.icons.rounded.ZoomOut
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BatteryState
import com.example.model.IndividualWidgetConfig
import com.example.model.WeatherState
import com.example.model.WidgetShape
import com.example.model.WidgetSize
import com.example.model.WidgetType
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import com.example.model.LauncherThemeMode
import com.example.ui.theme.rememberColorOsPalette
import androidx.compose.ui.draw.shadow

/**
 * Universal container for any Home Widget respecting individual Shape, Scale, Dimensions, ColorOS Theme & Blur Opacity.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndividualWidgetWrapper(
    config: IndividualWidgetConfig,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    themeMode: LauncherThemeMode = LauncherThemeMode.DARK_AMOLED,
    surfaceOpacity: Float = 0.55f,
    blurRadiusDp: Float = 24f,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val palette = rememberColorOsPalette(themeMode = themeMode, surfaceOpacity = surfaceOpacity)

    val cornerShape: Shape = when (config.shape) {
        WidgetShape.ROUNDED_SQUIRCLE -> RoundedCornerShape(22.dp)
        WidgetShape.PILL -> RoundedCornerShape(36.dp)
        WidgetShape.SHARP_MODERN -> RoundedCornerShape(12.dp)
        WidgetShape.CIRCLE -> CircleShape
    }

    val baseWidthDp = when (config.size) {
        WidgetSize.HORIZONTAL -> 340.dp
        WidgetSize.SQUARE -> 160.dp
        WidgetSize.VERTICAL -> 160.dp
        WidgetSize.COMPACT -> 340.dp
    }

    val baseHeightDp = when (config.size) {
        WidgetSize.HORIZONTAL -> 155.dp
        WidgetSize.SQUARE -> 155.dp
        WidgetSize.VERTICAL -> 320.dp
        WidgetSize.COMPACT -> 80.dp
    }

    val finalWidth = baseWidthDp * config.horizontalScale * config.scale
    val finalHeight = baseHeightDp * config.verticalScale * config.scale

    val shadowElevation = if (blurRadiusDp > 0f) (blurRadiusDp * 0.4f).coerceIn(4f, 16f).dp else 0.dp

    Box(
        modifier = modifier
            .width(finalWidth)
            .height(finalHeight)
            .shadow(
                elevation = shadowElevation,
                shape = cornerShape,
                ambientColor = if (palette.isDark) Color.Black.copy(alpha = 0.6f) else Color(0x30000000),
                spotColor = if (palette.isDark) Color.Black.copy(alpha = 0.4f) else Color(0x20000000)
            )
            .clip(cornerShape)
            .background(
                Brush.verticalGradient(
                    colors = if (palette.isDark) listOf(
                        Color(0xFF1E222A).copy(alpha = surfaceOpacity.coerceIn(0.20f, 0.95f)),
                        Color(0xFF12141A).copy(alpha = (surfaceOpacity + 0.10f).coerceIn(0.25f, 0.98f))
                    ) else listOf(
                        Color(0xFFFFFFFF).copy(alpha = surfaceOpacity.coerceIn(0.40f, 0.95f)),
                        Color(0xFFF1F5F9).copy(alpha = (surfaceOpacity + 0.15f).coerceIn(0.50f, 0.98f))
                    )
                )
            )
            .border(
                0.75.dp,
                if (palette.isDark) Color.White.copy(alpha = (surfaceOpacity * 0.35f).coerceIn(0.08f, 0.30f))
                else Color.Black.copy(alpha = 0.08f),
                cornerShape
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(14.dp)
    ) {
        content()
    }
}

// -------------------------------------------------------------
// 1. CLOCK & DATE WIDGET
// -------------------------------------------------------------
@Composable
fun ClockWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily
) {
    var timeStr by remember { mutableStateOf("") }
    var dateStr by remember { mutableStateOf("") }
    var secondsStr by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val tf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val df = SimpleDateFormat("d MMMM EEEE", Locale("tr"))
        val sf = SimpleDateFormat("ss", Locale.getDefault())
        while (true) {
            val now = Date()
            timeStr = tf.format(now)
            dateStr = df.format(now)
            secondsStr = sf.format(now)
            delay(1000)
        }
    }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dateStr.ifEmpty { "19 Eylül Cumartesi" },
                        fontSize = 12.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFF9500)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = timeStr.ifEmpty { "12:00" },
                            fontSize = 44.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = ":$secondsStr",
                            fontSize = 18.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Light,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    Text(
                        text = "İstanbul, Türkiye",
                        fontSize = 11.sp,
                        fontFamily = fontFamily,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }

                // Dual world time badge
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.07f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("Londra", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                    Text("11:42", fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tokyo", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                    Text("20:42", fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
        WidgetSize.SQUARE -> {
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
                        text = "SAAT",
                        fontSize = 11.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF9500),
                        letterSpacing = 0.5.sp
                    )
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = Color(0xFFFF9500),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = timeStr.ifEmpty { "12:00" },
                        fontSize = 38.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = dateStr.ifEmpty { "Cumartesi" },
                        fontSize = 11.5.sp,
                        fontFamily = fontFamily,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
                Text(
                    text = "Alarm: 07:30",
                    fontSize = 11.sp,
                    fontFamily = fontFamily,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
        WidgetSize.VERTICAL -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text("DİJİTAL SAAT", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFF9500))
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = timeStr.split(":").firstOrNull() ?: "12",
                        fontSize = 58.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = timeStr.split(":").lastOrNull() ?: "00",
                        fontSize = 58.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                Text(dateStr, fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.8f))
            }
        }
        WidgetSize.COMPACT -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = timeStr.ifEmpty { "12:00" }, fontSize = 28.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = dateStr, fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

// -------------------------------------------------------------
// 2. WEATHER WIDGET (Supports Square, Horizontal, Vertical)
// -------------------------------------------------------------
@Composable
fun WeatherWidgetContent(
    weatherState: WeatherState,
    size: WidgetSize,
    fontFamily: FontFamily
) {
    val temp = weatherState.temperatureCelsius
    val high = temp + 3
    val low = (temp - 3).coerceAtLeast(0)

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Current Weather
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = weatherState.city,
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        text = "$temp°",
                        fontSize = 42.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${weatherState.condition} • Y:$high° D:$low°",
                        fontSize = 11.5.sp,
                        fontFamily = fontFamily,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Right Hourly Forecast bar
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val hourly = listOf(
                        Triple("14:00", Icons.Rounded.WbSunny, "$temp°"),
                        Triple("16:00", Icons.Rounded.WbSunny, "${temp - 1}°"),
                        Triple("18:00", Icons.Rounded.WbSunny, "${temp - 3}°")
                    )
                    hourly.forEach { (hour, icon, htemp) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(hour, fontSize = 10.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Icon(icon, contentDescription = null, tint = Color(0xFFFFD60A), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(htemp, fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }
                }
            }
        }
        WidgetSize.SQUARE -> {
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
                        text = weatherState.city,
                        fontSize = 13.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Rounded.WbSunny,
                        contentDescription = null,
                        tint = Color(0xFFFFD60A),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Text(
                    text = "$temp°",
                    fontSize = 38.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Column {
                    Text(
                        text = weatherState.condition,
                        fontSize = 12.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "Y:$high°  D:$low°",
                        fontSize = 11.sp,
                        fontFamily = fontFamily,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                }
            }
        }
        WidgetSize.VERTICAL -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(weatherState.city, fontSize = 15.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("$temp°", fontSize = 44.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(weatherState.condition, fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.8f))
                }
                // 3 Day Forecast
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Pzt" to "${temp + 1}°", "Sal" to "$temp°", "Çar" to "${temp - 2}°").forEach { (day, itemTemp) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(day, fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.7f))
                            Text(itemTemp, fontSize = 12.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
        WidgetSize.COMPACT -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.WbSunny, contentDescription = null, tint = Color(0xFFFFD60A), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${weatherState.city}: $temp°", fontSize = 16.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Text(weatherState.condition, fontSize = 13.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.7f))
            }
        }
    }
}

// -------------------------------------------------------------
// 3. BATTERY & ECO WIDGET (Supports Square, Horizontal, Vertical)
// -------------------------------------------------------------
@Composable
fun BatteryWidgetContent(
    batteryState: BatteryState,
    size: WidgetSize,
    fontFamily: FontFamily,
    onToggleEcoMode: () -> Unit = {}
) {
    val isCharging = batteryState.isCharging
    val level = batteryState.levelPercent
    val levelColor = when {
        isCharging -> Color(0xFF34C759)
        level <= 20 -> Color(0xFFFF453A)
        level <= 40 -> Color(0xFFFFD60A)
        else -> Color(0xFF34C759)
    }

    var isEcoActive by remember { mutableStateOf(false) }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Phone battery status
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(54.dp)) {
                        CircularProgressIndicator(
                            progress = { level / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = levelColor,
                            trackColor = Color.White.copy(alpha = 0.12f),
                            strokeWidth = 5.dp
                        )
                        Icon(
                            imageVector = if (isCharging) Icons.Rounded.BatteryChargingFull else Icons.Rounded.BatteryFull,
                            contentDescription = null,
                            tint = levelColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Telefon",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.65f)
                        )
                        Text(
                            text = "%$level",
                            fontSize = 24.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = if (isCharging) "Hızlı Şarj Oluyor" else if (isEcoActive) "Güç Tasarrufu Açık" else "Normal Mod",
                            fontSize = 11.sp,
                            fontFamily = fontFamily,
                            color = if (isEcoActive) Color(0xFFFFD60A) else Color.White.copy(alpha = 0.6f)
                        )
                    }
                }

                // Connected Headphones Battery (Eco toggle or device)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.08f))
                        .clickable(onClick = {
                            isEcoActive = !isEcoActive
                            onToggleEcoMode()
                        })
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Headphones,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("%95", fontSize = 13.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Kulaklık", fontSize = 9.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                }
            }
        }
        WidgetSize.SQUARE -> {
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
                        text = "PİL DURUMU",
                        fontSize = 11.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 0.5.sp
                    )
                    Icon(
                        imageVector = if (isCharging) Icons.Rounded.BatteryChargingFull else Icons.Rounded.BatteryFull,
                        contentDescription = null,
                        tint = levelColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
                        CircularProgressIndicator(
                            progress = { level / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = levelColor,
                            trackColor = Color.White.copy(alpha = 0.12f),
                            strokeWidth = 4.5.dp
                        )
                        Text(
                            text = "$level%",
                            fontSize = 13.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isCharging) "Şarjda" else "Kalan süre",
                            fontSize = 11.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        Text(
                            text = if (isCharging) "Doluya 40 dk" else "~12 saat",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                // Eco mode button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isEcoActive) Color(0xFFFFD60A).copy(alpha = 0.2f) else Color.White.copy(alpha = 0.08f))
                        .clickable(onClick = {
                            isEcoActive = !isEcoActive
                            onToggleEcoMode()
                        })
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isEcoActive) "Eko Mod: Açık" else "Eko Modu Aç",
                        fontSize = 10.5.sp,
                        fontFamily = fontFamily,
                        color = if (isEcoActive) Color(0xFFFFD60A) else Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        WidgetSize.VERTICAL -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("PİL ANALİZİ", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.7f))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(90.dp)) {
                    CircularProgressIndicator(
                        progress = { level / 100f },
                        modifier = Modifier.fillMaxSize(),
                        color = levelColor,
                        trackColor = Color.White.copy(alpha = 0.12f),
                        strokeWidth = 8.dp
                    )
                    Text("%$level", fontSize = 22.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isCharging) "Şarj Ediliyor" else "Pil Sağlığı: Mükemmel", fontSize = 11.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.8f))
                    Text("Sıcaklık: ${batteryState.temperatureCelsius}°C", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                }
            }
        }
        WidgetSize.COMPACT -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pil Seviyesi: %$level", fontSize = 15.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                Icon(Icons.Rounded.BatteryFull, contentDescription = null, tint = levelColor, modifier = Modifier.size(22.dp))
            }
        }
    }
}

// -------------------------------------------------------------
// 4. MUSIC & MEDIA PLAYER WIDGET (+6)
// -------------------------------------------------------------
@Composable
fun MediaWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentProgress by remember { mutableStateOf(0.42f) }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Album artwork
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MusicNote,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Track details & player slider
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Midnight City",
                        fontSize = 14.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "M83 • Hurry Up, We're Dreaming",
                        fontSize = 11.sp,
                        fontFamily = fontFamily,
                        color = Color.White.copy(alpha = 0.65f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Progress line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(currentProgress)
                                .fillMaxHeight()
                                .background(Color(0xFF007AFF))
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("01:42", fontSize = 9.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                        Text("04:03", fontSize = 9.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Playback controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { currentProgress = (currentProgress - 0.1f).coerceAtLeast(0f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Rounded.FastRewind, contentDescription = "Geri", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = "Oynat",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { currentProgress = (currentProgress + 0.1f).coerceAtMost(1f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Rounded.FastForward, contentDescription = "İleri", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
        WidgetSize.SQUARE -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(Color(0xFFFF416C), Color(0xFFFF4B2B)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    IconButton(
                        onClick = { isPlaying = !isPlaying },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                }
                Column {
                    Text("Starboy", fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("The Weeknd", fontSize = 11.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.65f))
                }
                // Scrubber
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Box(modifier = Modifier.fillMaxWidth(0.55f).fillMaxHeight().background(Color(0xFFFF416C)))
                }
            }
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Müzik Çalar: Starboy", fontSize = 14.sp, fontFamily = fontFamily, color = Color.White)
                Icon(if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White)
            }
        }
    }
}

// -------------------------------------------------------------
// 5. QUICK SYSTEM CONTROLS WIDGET (+6)
// -------------------------------------------------------------
@Composable
fun ControlsWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily,
    isTorchOn: Boolean,
    onToggleTorch: () -> Unit
) {
    var isWifiOn by remember { mutableStateOf(true) }
    var isBtOn by remember { mutableStateOf(true) }
    var isSilentOn by remember { mutableStateOf(false) }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlTile(icon = Icons.Rounded.Wifi, title = "Wi-Fi", isActive = isWifiOn, onClick = { isWifiOn = !isWifiOn })
                ControlTile(icon = Icons.Rounded.Bluetooth, title = "Bluetooth", isActive = isBtOn, onClick = { isBtOn = !isBtOn })
                ControlTile(icon = if (isTorchOn) Icons.Rounded.FlashlightOn else Icons.Rounded.FlashlightOff, title = "Fener", isActive = isTorchOn, activeColor = Color(0xFFFFD60A), onClick = onToggleTorch)
                ControlTile(icon = if (isSilentOn) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications, title = "Sessiz", isActive = isSilentOn, activeColor = Color(0xFFFF453A), onClick = { isSilentOn = !isSilentOn })
            }
        }
        WidgetSize.SQUARE -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ControlTile(icon = Icons.Rounded.Wifi, title = "Wi-Fi", isActive = isWifiOn, onClick = { isWifiOn = !isWifiOn })
                    ControlTile(icon = Icons.Rounded.Bluetooth, title = "BT", isActive = isBtOn, onClick = { isBtOn = !isBtOn })
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ControlTile(icon = if (isTorchOn) Icons.Rounded.FlashlightOn else Icons.Rounded.FlashlightOff, title = "Fener", isActive = isTorchOn, activeColor = Color(0xFFFFD60A), onClick = onToggleTorch)
                    ControlTile(icon = if (isSilentOn) Icons.Rounded.NotificationsOff else Icons.Rounded.Notifications, title = "Sessiz", isActive = isSilentOn, activeColor = Color(0xFFFF453A), onClick = { isSilentOn = !isSilentOn })
                }
            }
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ControlTile(icon = Icons.Rounded.Wifi, title = "Wi-Fi", isActive = isWifiOn, onClick = { isWifiOn = !isWifiOn })
                ControlTile(icon = Icons.Rounded.Bluetooth, title = "BT", isActive = isBtOn, onClick = { isBtOn = !isBtOn })
                ControlTile(icon = if (isTorchOn) Icons.Rounded.FlashlightOn else Icons.Rounded.FlashlightOff, title = "Fener", isActive = isTorchOn, onClick = onToggleTorch)
            }
        }
    }
}

@Composable
private fun ControlTile(
    icon: ImageVector,
    title: String,
    isActive: Boolean,
    activeColor: Color = Color(0xFF007AFF),
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (isActive) activeColor else Color.White.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isActive) Color.White else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}

// -------------------------------------------------------------
// 6. CALENDAR & AGENDA WIDGET (+6)
// -------------------------------------------------------------
@Composable
fun CalendarWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily
) {
    var todayDay by remember { mutableStateOf("19") }
    var todayName by remember { mutableStateOf("Cumartesi") }

    LaunchedEffect(Unit) {
        val df = SimpleDateFormat("d", Locale.getDefault())
        val nf = SimpleDateFormat("EEEE", Locale("tr"))
        todayDay = df.format(Date())
        todayName = nf.format(Date())
    }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Day Badge
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFFF3B30))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(todayName.take(3).uppercase(), fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(todayDay, fontSize = 28.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Right Agenda List
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("YAKLAŞAN ETKİNLİKLER", fontSize = 10.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFF3B30))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF007AFF)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("15:30 • Tasarım İnceleme Toplantısı", fontSize = 12.sp, fontFamily = fontFamily, fontWeight = FontWeight.Medium, color = Color.White)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF34C759)))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("18:00 • Akşam Spor & Yürüyüş", fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.75f))
                    }
                }
            }
        }
        WidgetSize.SQUARE -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(todayName.uppercase(), fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFF3B30))
                    Icon(Icons.Rounded.Event, contentDescription = null, tint = Color(0xFFFF3B30), modifier = Modifier.size(16.dp))
                }
                Text(todayDay, fontSize = 42.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                Text("15:30 Tasarım Toplantısı", fontSize = 11.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.8f))
            }
        }
        else -> {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text("Takvim: $todayDay $todayName", fontSize = 14.sp, fontFamily = fontFamily, color = Color.White)
            }
        }
    }
}

// -------------------------------------------------------------
// 7. NOTES & INSPIRATION WIDGET (+6)
// -------------------------------------------------------------
@Composable
fun NotesWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily
) {
    val tasks = remember {
        mutableStateListOf(
            "Yeni başlatıcı temasını dene" to true,
            "Fotoğrafları düzenle" to false,
            "Su içmeyi unutma" to false
        )
    }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.FormatQuote, contentDescription = null, tint = Color(0xFFFFD60A), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("GÜNÜN İLHAMI", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFFD60A))
                    }
                    Text("Not Defteri", fontSize = 10.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                }
                Text(
                    text = "“Küçük adımlar, zamanla en büyük yolculuklara dönüşür.”",
                    fontSize = 13.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.95f)
                )
                Text(
                    text = "Düzenlemek veya yeni not eklemek için basılı tutun.",
                    fontSize = 10.sp,
                    fontFamily = fontFamily,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
        WidgetSize.SQUARE -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("GÜNÜN SÖZÜ", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFFD60A))
                Text(
                    text = "“Odaklan ve anı yaşa.”",
                    fontSize = 16.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text("Bugün harika bir gün!", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.65f))
            }
        }
        WidgetSize.VERTICAL -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Text("GÖREV LİSTESİ", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFFFFD60A))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tasks.forEachIndexed { index, (task, isDone) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { tasks[index] = task to !isDone }
                        ) {
                            Icon(
                                imageVector = if (isDone) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isDone) Color(0xFF34C759) else Color.White.copy(alpha = 0.5f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = task,
                                fontSize = 11.5.sp,
                                fontFamily = fontFamily,
                                color = if (isDone) Color.White.copy(alpha = 0.5f) else Color.White
                            )
                        }
                    }
                }
                Text("+ Yeni Görev Ekle", fontSize = 11.sp, fontFamily = fontFamily, color = Color(0xFF007AFF))
            }
        }
        WidgetSize.COMPACT -> {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text("Not: “Küçük adımlar büyük başarılar getirir.”", fontSize = 12.5.sp, fontFamily = fontFamily, color = Color.White)
            }
        }
    }
}

// -------------------------------------------------------------
// 8. SPEED DIAL & CONTACTS WIDGET (+6)
// -------------------------------------------------------------
@Composable
fun SpeedDialWidgetContent(
    size: WidgetSize,
    fontFamily: FontFamily,
    context: Context
) {
    val contacts = listOf(
        Pair("Aile", Color(0xFF007AFF)),
        Pair("Ev", Color(0xFF34C759)),
        Pair("İş", Color(0xFFFF9500)),
        Pair("Acil", Color(0xFFFF3B30))
    )

    fun dialContact() {
        try {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) {}
    }

    when (size) {
        WidgetSize.HORIZONTAL -> {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                contacts.forEach { (name, color) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { dialContact() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(color),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(name.take(1), fontSize = 18.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(name, fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
        WidgetSize.SQUARE -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("HIZLI ARAMA", fontSize = 11.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color(0xFF34C759))
                    Icon(Icons.Rounded.Call, contentDescription = null, tint = Color(0xFF34C759), modifier = Modifier.size(16.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    contacts.take(2).forEach { (name, color) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { dialContact() }) {
                            Box(modifier = Modifier.size(42.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                                Text(name.take(1), fontSize = 16.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(name, fontSize = 10.5.sp, fontFamily = fontFamily, color = Color.White)
                        }
                    }
                }
                Text("Rehberi Aç", fontSize = 10.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f), modifier = Modifier.clickable { dialContact() })
            }
        }
        else -> {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                Text("Hızlı Arama Kısayolları", fontSize = 14.sp, fontFamily = fontFamily, color = Color.White)
            }
        }
    }
}

// -------------------------------------------------------------
// INDIVIDUAL WIDGET EDIT & SIZING DIALOG
// (Dikdörtgen, dikey, yatay dikdörtgen boyutları ve şekilleri)
// -------------------------------------------------------------
@Composable
fun IndividualWidgetEditDialog(
    config: IndividualWidgetConfig,
    fontFamily: FontFamily,
    onSizeChange: (WidgetSize) -> Unit,
    onShapeChange: (WidgetShape) -> Unit,
    onScaleChange: (Float) -> Unit,
    onHorizontalScaleChange: (Float) -> Unit = {},
    onVerticalScaleChange: (Float) -> Unit = {},
    onDeleteWidget: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF1C1D21))
                .border(0.5.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = config.type.titleTr,
                            fontSize = 18.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Bileşeni özelleştir veya boyutlandır",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Text("Bitti", fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, color = Color(0xFF007AFF))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 1. BOYUT SEÇİMİ (Kare, Yatay Dikdörtgen, Dikey Dikdörtgen)
                Text(
                    text = "Bileşen Boyutu (Şablon)",
                    fontSize = 13.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        WidgetSize.SQUARE to "Kare (2x2)",
                        WidgetSize.HORIZONTAL to "Yatay (4x2)",
                        WidgetSize.VERTICAL to "Dikey (2x4)"
                    ).forEach { (size, label) ->
                        val isSelected = config.size == size
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.08f))
                                .clickable { onSizeChange(size) }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. YATAYLAMA (GENİŞLİK) & DİKEYLEME (YÜKSEKLİK) KÜÇÜLTÜP BÜYÜLTME
                Text(
                    text = "Yatay Genişlik (En): ${(config.horizontalScale * 100).toInt()}%",
                    fontSize = 12.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Dar", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                    Slider(
                        value = config.horizontalScale,
                        onValueChange = onHorizontalScaleChange,
                        valueRange = 0.6f..1.4f,
                        modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF34C759),
                            activeTrackColor = Color(0xFF34C759),
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                    Text("Geniş", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Dikey Yükseklik (Boy): ${(config.verticalScale * 100).toInt()}%",
                    fontSize = 12.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Kısa", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                    Slider(
                        value = config.verticalScale,
                        onValueChange = onVerticalScaleChange,
                        valueRange = 0.6f..1.4f,
                        modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFFF9500),
                            activeTrackColor = Color(0xFFFF9500),
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                    Text("Uzun", fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.5f))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. ŞEKİL SEÇİMİ (Squircle, Pill, Sharp, Circle)
                Text(
                    text = "Bileşen Köşe Şekli",
                    fontSize = 12.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WidgetShape.values().forEach { shape ->
                        val isSelected = config.shape == shape
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.08f))
                                .clickable { onShapeChange(shape) }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = shape.titleTr,
                                fontSize = 10.sp,
                                fontFamily = fontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4. BİLEŞENİ SİL / KALDIR BUTONU
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF3B30).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFFF3B30).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .clickable {
                            onDeleteWidget()
                            onDismiss()
                        }
                        .padding(vertical = 10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Sil", tint = Color(0xFFFF453A), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bileşeni Ana Ekrandan Kaldır (Sil)", fontSize = 13.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, color = Color(0xFFFF453A))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// WIDGET LIBRARY ADD SHEET (+6 Farklı Widget Ekleme Menüsü)
// -------------------------------------------------------------
@Composable
fun AddWidgetCatalogSheet(
    fontFamily: FontFamily,
    onAddWidget: (WidgetType, WidgetSize) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedType by remember { mutableStateOf(WidgetType.WEATHER) }
    var selectedSize by remember { mutableStateOf(WidgetSize.SQUARE) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF18191C))
                .border(0.5.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Widget Galerisi", fontSize = 19.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("+6 farklı zengin bileşen çeşidi", fontSize = 12.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Rounded.Close, contentDescription = "Kapat", tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Scrollable List of Widget Types
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(WidgetType.values()) { type ->
                        val isSelected = selectedType == type
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) Color(0xFF007AFF).copy(alpha = 0.25f) else Color.White.copy(alpha = 0.05f))
                                .border(
                                    if (isSelected) 1.dp else 0.5.dp,
                                    if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.10f),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    selectedType = type
                                    selectedSize = type.defaultSize
                                }
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(type.titleTr, fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(type.subtitleTr, fontSize = 11.5.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
                                }
                                if (isSelected) {
                                    Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF007AFF), modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Size Selector for Chosen Widget
                Text("Eklenecek Boyut:", fontSize = 12.5.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.85f))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        WidgetSize.SQUARE to "Kare (2x2)",
                        WidgetSize.HORIZONTAL to "Yatay (4x2)",
                        WidgetSize.VERTICAL to "Dikey (2x4)"
                    ).forEach { (size, label) ->
                        val isChosen = selectedSize == size
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isChosen) Color(0xFF007AFF) else Color.White.copy(alpha = 0.08f))
                                .clickable { selectedSize = size }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(label, fontSize = 11.sp, fontFamily = fontFamily, fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Add Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF007AFF))
                        .clickable {
                            onAddWidget(selectedType, selectedSize)
                            onDismiss()
                        }
                        .padding(vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ana Ekrana Ekle", fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
