package com.example.ui.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.InvertColors
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IconShape
import com.example.model.IconThemePack
import com.example.model.LiquidWallpaperType
import com.example.model.PerformanceMode
import com.example.ui.liquid.LiquidGlassCard
import com.example.ui.liquid.LiquidGlassTheme
import com.example.viewmodel.LauncherUiState

enum class SettingsTab(val titleTr: String) {
    LIQUID_FX("Sulu Cam FX"),
    ICONS("Simgeler"),
    WALLPAPERS("Duvar Kağıtları"),
    ENGINE("Motor & Pil")
}

@Composable
fun TvnahSettingsSheet(
    state: LauncherUiState,
    onClose: () -> Unit,
    onSetIconShape: (IconShape) -> Unit,
    onSetIconThemePack: (IconThemePack) -> Unit,
    onSetIconSize: (Float) -> Unit,
    onToggleShowLabels: (Boolean) -> Unit,
    onSetWallpaper: (LiquidWallpaperType) -> Unit,
    onSetPerformanceMode: (PerformanceMode) -> Unit,
    onToggleWaterRipple: () -> Unit,
    onSetDefaultLauncher: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(SettingsTab.LIQUID_FX) }

    AnimatedVisibility(
        visible = state.isSettingsOpen,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFA051224),
                            Color(0xFD040E1B),
                            Color(0xFF02070E)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF00F0FF).copy(alpha = 0.2f))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription = "Tvnah Studio",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Tvnah Studio",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Aero-Hydra Sıvı Cam Özelleştirme",
                                fontSize = 11.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Tabs
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(SettingsTab.values()) { tab ->
                        val isSelected = selectedTab == tab
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.28f)
                                    else Color.White.copy(alpha = 0.07f)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.7f)
                                    else Color.White.copy(alpha = 0.12f),
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { selectedTab = tab }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = tab.titleTr,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF00F0FF) else Color(0xFFCFD8DC)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tab Content
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (selectedTab) {
                        SettingsTab.LIQUID_FX -> {
                            item {
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.2f
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "Sulu Cam & Kırılma Optiği",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Ekrandaki tüm kartlar ve widgetlar için prizmatik ışık kırılmaları, menisküs yansımaları ve su damlası tepkileri.",
                                            fontSize = 12.sp,
                                            color = Color(0xFF90A4AE)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Ripple toggle
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = "Dokunmatik Su Dalgası",
                                                    fontSize = 13.5.sp,
                                                    fontWeight = FontWeight.Medium,
                                                    color = Color.White
                                                )
                                                Text(
                                                    text = "Ekrana dokunulduğunda yayılan sıvı dalgalanmaları",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF80DEEA)
                                                )
                                            }
                                            Switch(
                                                checked = state.waterRippleEnabled,
                                                onCheckedChange = { onToggleWaterRipple() },
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color(0xFF00F0FF),
                                                    checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f)
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                // Default launcher action card
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.22f,
                                    glowAccentColor = Color(0xFF00F0FF),
                                    onClick = onSetDefaultLauncher
                                ) {
                                    Row(
                                        modifier = Modifier.padding(18.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .size(42.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF00F0FF).copy(alpha = 0.25f))
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Home,
                                                contentDescription = "Default Launcher",
                                                tint = Color(0xFF00F0FF),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(14.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "Varsayılan Başlatıcı Yap",
                                                fontSize = 14.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "Tvnah Launcher'ı ana ekranınız olarak ayarlayın",
                                                fontSize = 11.5.sp,
                                                color = Color(0xFF80DEEA)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        SettingsTab.ICONS -> {
                            item {
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.2f
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "Simge Şekli (Sıvı Form)",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconShape.values().forEach { shape ->
                                                val isSelected = state.iconShape == shape
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(
                                                            if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.22f)
                                                            else Color.White.copy(alpha = 0.05f)
                                                        )
                                                        .border(
                                                            1.dp,
                                                            if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.6f)
                                                            else Color.White.copy(alpha = 0.08f),
                                                            RoundedCornerShape(14.dp)
                                                        )
                                                        .clickable { onSetIconShape(shape) }
                                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                                .clip(LiquidGlassTheme.getShapeForIcon(shape))
                                                                .background(Color(0xFF00F0FF))
                                                        )
                                                        Spacer(modifier = Modifier.width(12.dp))
                                                        Text(
                                                            text = shape.titleTr,
                                                            fontSize = 13.5.sp,
                                                            color = Color.White,
                                                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                                        )
                                                    }
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.Check,
                                                            contentDescription = "Selected",
                                                            tint = Color(0xFF00F0FF),
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.2f
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "Simge Cam Filtresi & Renk Teması",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                            IconThemePack.values().forEach { pack ->
                                                val isSelected = state.iconThemePack == pack
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(
                                                            if (isSelected) pack.primaryTint.copy(alpha = 0.22f)
                                                            else Color.White.copy(alpha = 0.05f)
                                                        )
                                                        .border(
                                                            1.dp,
                                                            if (isSelected) pack.primaryTint.copy(alpha = 0.7f)
                                                            else Color.White.copy(alpha = 0.08f),
                                                            RoundedCornerShape(14.dp)
                                                        )
                                                        .clickable { onSetIconThemePack(pack) }
                                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(20.dp)
                                                                .clip(CircleShape)
                                                                .background(pack.primaryTint)
                                                        )
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Column {
                                                            Text(
                                                                text = pack.titleTr,
                                                                fontSize = 13.5.sp,
                                                                color = Color.White,
                                                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                                            )
                                                            Text(
                                                                text = pack.description,
                                                                fontSize = 10.5.sp,
                                                                color = Color(0xFF90A4AE)
                                                            )
                                                        }
                                                    }
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.Check,
                                                            contentDescription = "Selected",
                                                            tint = pack.primaryTint,
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.2f
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Text(
                                            text = "Simge Boyutu & Etiketler",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(text = "Boyut", fontSize = 13.sp, color = Color(0xFF80DEEA))
                                            Text(text = "${state.iconSize.toInt()} dp", fontSize = 13.sp, color = Color.White)
                                        }

                                        Slider(
                                            value = state.iconSize,
                                            onValueChange = onSetIconSize,
                                            valueRange = 48f..68f,
                                            colors = SliderDefaults.colors(
                                                thumbColor = Color(0xFF00F0FF),
                                                activeTrackColor = Color(0xFF00F0FF)
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Uygulama İsimlerini Göster", fontSize = 13.5.sp, color = Color.White)
                                            Switch(
                                                checked = state.showLabels,
                                                onCheckedChange = onToggleShowLabels,
                                                colors = SwitchDefaults.colors(
                                                    checkedThumbColor = Color(0xFF00F0FF),
                                                    checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        SettingsTab.WALLPAPERS -> {
                            item {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(
                                        text = "Özel Sıvı Cam Duvar Kağıtları",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Akıcı su optiği ile derinlik katan benzersiz temalar.",
                                        fontSize = 12.sp,
                                        color = Color(0xFF90A4AE)
                                    )

                                    LiquidWallpaperType.values().forEach { wallpaper ->
                                        val isSelected = state.selectedWallpaper == wallpaper
                                        LiquidGlassCard(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(18.dp),
                                            blurRefractionAlpha = if (isSelected) 0.28f else 0.14f,
                                            glowAccentColor = wallpaper.accentColor,
                                            onClick = { onSetWallpaper(wallpaper) }
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(14.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(
                                                                brush = Brush.linearGradient(
                                                                    listOf(
                                                                        wallpaper.primaryColor,
                                                                        wallpaper.secondaryColor,
                                                                        wallpaper.accentColor
                                                                    )
                                                                )
                                                            )
                                                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                                    )
                                                    Spacer(modifier = Modifier.width(12.dp))
                                                    Column {
                                                        Text(
                                                            text = wallpaper.titleTr,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = Color.White
                                                        )
                                                        Text(
                                                            text = "Dinamik Caustics Yansıması",
                                                            fontSize = 11.sp,
                                                            color = Color(0xFF80DEEA)
                                                        )
                                                    }
                                                }
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = "Selected",
                                                        tint = Color(0xFF00F0FF),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        SettingsTab.ENGINE -> {
                            item {
                                LiquidGlassCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    blurRefractionAlpha = 0.2f
                                ) {
                                    Column(modifier = Modifier.padding(18.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.Speed,
                                                contentDescription = "Engine",
                                                tint = Color(0xFF00F0FF),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Performans ve Batarya Motoru",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Cihazınızın donanım kapasitesine göre render motorunu ve yenileme sıklığını optimize edin.",
                                            fontSize = 12.sp,
                                            color = Color(0xFF90A4AE)
                                        )

                                        Spacer(modifier = Modifier.height(14.dp))

                                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            PerformanceMode.values().forEach { mode ->
                                                val isSelected = state.performanceMode == mode
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(14.dp))
                                                        .background(
                                                            if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.24f)
                                                            else Color.White.copy(alpha = 0.05f)
                                                        )
                                                        .border(
                                                            1.dp,
                                                            if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.7f)
                                                            else Color.White.copy(alpha = 0.08f),
                                                            RoundedCornerShape(14.dp)
                                                        )
                                                        .clickable { onSetPerformanceMode(mode) }
                                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = mode.titleTr,
                                                            fontSize = 13.5.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = Color.White
                                                        )
                                                        Text(
                                                            text = mode.description,
                                                            fontSize = 11.sp,
                                                            color = Color(0xFF80DEEA)
                                                        )
                                                    }
                                                    if (isSelected) {
                                                        Icon(
                                                            imageVector = Icons.Rounded.Check,
                                                            contentDescription = "Selected",
                                                            tint = Color(0xFF00F0FF),
                                                            modifier = Modifier.size(18.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
