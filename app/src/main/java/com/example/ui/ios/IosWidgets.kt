package com.example.ui.ios

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.FlashlightOff
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material.icons.rounded.ZoomOut
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import com.example.model.LauncherThemeMode
import com.example.ui.theme.rememberColorOsPalette
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
import androidx.compose.ui.window.Dialog
import com.example.model.BatteryState
import com.example.model.WeatherState
import com.example.model.WidgetShape
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modern Minimalist Lockscreen & SpringBoard Clock & Date Header.
 * Fully customizable size and font weight.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IosClockHeader(
    fontFamily: FontFamily,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSizeSp: Float = 68f,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
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
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
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
 * Helper to compute RoundedCornerShape from WidgetShape enum
 */
fun getShapeFromWidgetShape(shape: WidgetShape, baseCornerDp: Float = 22f): RoundedCornerShape {
    return when (shape) {
        WidgetShape.ROUNDED_SQUIRCLE -> RoundedCornerShape(baseCornerDp.dp)
        WidgetShape.PILL -> RoundedCornerShape(36.dp)
        WidgetShape.SHARP_MODERN -> RoundedCornerShape(10.dp)
        WidgetShape.CIRCLE -> RoundedCornerShape(50)
    }
}

/**
 * 2x2 Weather Widget Card (Modern Matte Surface, customizable scale, shape, and long click).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IosWeatherWidget(
    weatherState: WeatherState,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    scale: Float = 1.0f,
    shape: WidgetShape = WidgetShape.ROUNDED_SQUIRCLE,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "weather_press_scale"
    )

    val widgetShape = getShapeFromWidgetShape(shape, 22f)
    val widthDp = (155 * scale).dp
    val heightDp = (148 * scale).dp

    Box(
        modifier = modifier
            .size(width = widthDp, height = heightDp)
            .scale(animatedScale)
            .shadow(8.dp, widgetShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(widgetShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), widgetShape)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
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
 * 2x2 Battery Widget Card (Modern Matte Surface, customizable scale, shape, and long click).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IosBatteryWidget(
    batteryState: BatteryState,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    scale: Float = 1.0f,
    shape: WidgetShape = WidgetShape.ROUNDED_SQUIRCLE,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 400f),
        label = "battery_press_scale"
    )

    val widgetShape = getShapeFromWidgetShape(shape, 22f)
    val widthDp = (155 * scale).dp
    val heightDp = (148 * scale).dp

    Box(
        modifier = modifier
            .size(width = widthDp, height = heightDp)
            .scale(animatedScale)
            .shadow(8.dp, widgetShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(widgetShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), widgetShape)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
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
 * Modern Search Pill with ColorOS theme and frosted glass support.
 */
@Composable
fun IosSearchPill(
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    themeMode: LauncherThemeMode = LauncherThemeMode.DARK_AMOLED,
    surfaceOpacity: Float = 0.55f,
    onClick: () -> Unit = {}
) {
    val palette = rememberColorOsPalette(themeMode = themeMode, surfaceOpacity = surfaceOpacity)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(palette.searchPillBackground)
            .border(0.5.dp, palette.surfaceBorderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Ara",
                tint = palette.primaryTextColor.copy(alpha = 0.85f),
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Ara",
                fontSize = 12.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = palette.primaryTextColor.copy(alpha = 0.90f)
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

/**
 * Widget Customization & Deletion Dialog.
 * Appears when long-pressing any widget (Clock, Weather, Battery).
 * Lets the user adjust scale, change shape (squircle, pill, sharp, round), or delete/hide the widget.
 */
@Composable
fun WidgetEditDialog(
    widgetTitle: String,
    currentScale: Float,
    currentShape: WidgetShape,
    fontFamily: FontFamily,
    onScaleChange: (Float) -> Unit,
    onShapeChange: (WidgetShape) -> Unit,
    onDeleteWidget: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF1E2024))
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
                            text = widgetTitle,
                            fontSize = 18.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Boyut, şekil ayarla veya kaldır",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            text = "Bitti",
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF007AFF)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 1. BOYUT / ÖLÇEK AYARI (Scale slider)
                Text(
                    text = "Bileşen Boyutu: ${(currentScale * 100).toInt()}%",
                    fontSize = 13.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ZoomOut,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                    Slider(
                        value = currentScale,
                        onValueChange = onScaleChange,
                        valueRange = 0.75f..1.30f,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF007AFF),
                            activeTrackColor = Color(0xFF007AFF),
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                        )
                    )
                    Icon(
                        imageVector = Icons.Rounded.ZoomIn,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 2. ŞEKİL AYARI (Widget Shapes: Squircle, Pill, Sharp, Circle)
                Text(
                    text = "Bileşen Şekli",
                    fontSize = 13.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WidgetShape.values().forEach { shape ->
                        val isSelected = shape == currentShape
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.08f)
                                )
                                .clickable { onShapeChange(shape) }
                                .padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = shape.titleTr,
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 3. WIDGET SİL / KALDIR BUTONU (Delete / Hide)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFE02020).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFE02020).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                        .clickable {
                            onDeleteWidget()
                            onDismiss()
                        }
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Widget Sil",
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bileşeni Ana Ekrandan Kaldır (Sil)",
                            fontSize = 13.5.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFFF5252)
                        )
                    }
                }
            }
        }
    }
}

