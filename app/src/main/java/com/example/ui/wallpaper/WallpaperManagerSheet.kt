package com.example.ui.wallpaper

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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.InvertColors
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.engine.SystemWallpaperTarget
import com.example.model.LiquidWallpaperConfig
import com.example.model.LiquidWallpaperType
import com.example.model.PerformanceMode
import com.example.ui.liquid.LiquidGlassCard
import com.example.ui.liquid.LiquidWallpaper

/**
 * Modern, interactive Liquid-Glass Wallpaper Manager sheet.
 * Provides live real-time interactive preview, fine-grained optics tuning,
 * preset gallery, and one-tap application to the Launcher AND Android OS System Theme.
 */
@Composable
fun WallpaperManagerSheet(
    isOpen: Boolean,
    currentConfig: LiquidWallpaperConfig,
    performanceMode: PerformanceMode,
    onClose: () -> Unit,
    onApplyConfig: (LiquidWallpaperConfig) -> Unit,
    modifier: Modifier = Modifier,
    onApplyAsSystemWallpaper: (LiquidWallpaperConfig, SystemWallpaperTarget) -> Unit = { _, _ -> },
    onLaunchLiveWallpaper: (LiquidWallpaperConfig) -> Unit = {},
    onSetDefaultLauncher: () -> Unit = {}
) {
    var draftConfig by remember(currentConfig, isOpen) { mutableStateOf(currentConfig) }
    var appliedLauncherNotification by remember { mutableStateOf(false) }
    var showSystemThemeDialog by remember { mutableStateOf(false) }
    var systemStatusMessage by remember { mutableStateOf<String?>(null) }

    AnimatedVisibility(
        visible = isOpen,
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
                            Color(0xF5051020),
                            Color(0xFA030B17),
                            Color(0xFF02060E)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF00F0FF).copy(alpha = 0.2f))
                                .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Wallpaper,
                                contentDescription = "Wallpaper Manager",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Sıvı Cam Duvar Kağıdı",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Compose Sıvı Optiği ve Kostik Motoru",
                                fontSize = 11.5.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Scrollable Content: Preview, Presets, Sliders
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Live Interactive Preview Card
                    item {
                        LiquidGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(26.dp),
                            blurRefractionAlpha = 0.25f,
                            glowAccentColor = draftConfig.wallpaperType.accentColor
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Visibility,
                                            contentDescription = "Preview",
                                            tint = draftConfig.wallpaperType.accentColor,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Canlı Sıvı Önizleme",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }

                                    // Refraction index pill
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(draftConfig.wallpaperType.accentColor.copy(alpha = 0.2f))
                                            .border(1.dp, draftConfig.wallpaperType.accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "n = 1.52 • ${performanceMode.fpsLimit} FPS",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = draftConfig.wallpaperType.accentColor
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Interactive mini-canvas viewport
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(190.dp)
                                        .clip(RoundedCornerShape(18.dp))
                                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(18.dp))
                                ) {
                                    // Running the actual Compose LiquidWallpaper inside preview
                                    LiquidWallpaper(
                                        config = draftConfig,
                                        performanceMode = performanceMode,
                                        enableInteractiveTouch = true
                                    )

                                    // Floating interactive hint overlay
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .padding(bottom = 10.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xD0030B17))
                                            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.WaterDrop,
                                                contentDescription = "Hint",
                                                tint = draftConfig.wallpaperType.accentColor,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Cam yüzeyine dokunarak sıvı kırılmasını test edin",
                                                fontSize = 11.sp,
                                                color = Color.White.copy(alpha = 0.9f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 2. Liquid Glass Presets Gallery
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sıvı Cam Temaları",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${LiquidWallpaperType.values().size} Özel Tasarım",
                                    fontSize = 11.sp,
                                    color = Color(0xFF80DEEA)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 2.dp)
                            ) {
                                items(LiquidWallpaperType.values()) { preset ->
                                    val isSelected = draftConfig.wallpaperType == preset
                                    LiquidGlassCard(
                                        modifier = Modifier.width(136.dp),
                                        shape = RoundedCornerShape(18.dp),
                                        blurRefractionAlpha = if (isSelected) 0.32f else 0.12f,
                                        glowAccentColor = preset.accentColor,
                                        onClick = {
                                            draftConfig = LiquidWallpaperConfig.defaultFor(preset)
                                        }
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(10.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            // Gradient optical orb
                                            Box(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        brush = Brush.radialGradient(
                                                            colors = listOf(
                                                                preset.accentColor,
                                                                preset.secondaryColor,
                                                                preset.primaryColor
                                                            )
                                                        )
                                                    )
                                                    .border(
                                                        width = if (isSelected) 2.dp else 1.dp,
                                                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.25f),
                                                        shape = CircleShape
                                                    )
                                            ) {
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Check,
                                                        contentDescription = "Selected",
                                                        tint = Color.White,
                                                        modifier = Modifier
                                                            .size(20.dp)
                                                            .align(Alignment.Center)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = preset.titleTr,
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = Color.White,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = preset.subtitleTr,
                                                fontSize = 9.5.sp,
                                                color = Color(0xFF90A4AE),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Fluid Physics & Glass Optics Controls
                    item {
                        LiquidGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            blurRefractionAlpha = 0.22f
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Rounded.Tune,
                                        contentDescription = "Optics Controls",
                                        tint = Color(0xFF00F0FF),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Cam Optiği & Akış Fiziği",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))

                                // Flow Speed Slider
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Akış Hızı (Flow Velocity)", fontSize = 12.5.sp, color = Color(0xFF80DEEA))
                                        Text(text = "%.1fx".format(draftConfig.flowSpeed), fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Slider(
                                        value = draftConfig.flowSpeed,
                                        onValueChange = { draftConfig = draftConfig.copy(flowSpeed = it) },
                                        valueRange = 0.2f..2.5f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color(0xFF00F0FF),
                                            activeTrackColor = Color(0xFF00F0FF)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Glass Thickness & Refraction Index Slider
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Cam Kırılma İndeksi & Derinlik", fontSize = 12.5.sp, color = Color(0xFF80DEEA))
                                        Text(text = "%.1fx".format(draftConfig.glassThickness), fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Slider(
                                        value = draftConfig.glassThickness,
                                        onValueChange = { draftConfig = draftConfig.copy(glassThickness = it) },
                                        valueRange = 0.5f..2.5f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = draftConfig.wallpaperType.accentColor,
                                            activeTrackColor = draftConfig.wallpaperType.accentColor
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Caustic Specular Glare Intensity
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Kostik Işık ve Parlama Şiddeti", fontSize = 12.5.sp, color = Color(0xFF80DEEA))
                                        Text(text = "%${(draftConfig.causticIntensity * 100).toInt()}", fontSize = 12.5.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Slider(
                                        value = draftConfig.causticIntensity,
                                        onValueChange = { draftConfig = draftConfig.copy(causticIntensity = it) },
                                        valueRange = 0.1f..1.0f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = Color.White,
                                            activeTrackColor = Color.White.copy(alpha = 0.7f)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Chromatic Aberration Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Prizmatik Renk Ayrışması",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Cam kenarlarında spektral gökkuşağı kırılımları",
                                            fontSize = 11.sp,
                                            color = Color(0xFF90A4AE)
                                        )
                                    }
                                    Switch(
                                        checked = draftConfig.chromaticAberration,
                                        onCheckedChange = { draftConfig = draftConfig.copy(chromaticAberration = it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color(0xFF00F0FF),
                                            checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Floating Liquid Glass Droplets Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Yüzen Sıvı Cam Damlaları",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Harmonik yörüngelerde yüzen 3D cam küreleri",
                                            fontSize = 11.sp,
                                            color = Color(0xFF90A4AE)
                                        )
                                    }
                                    Switch(
                                        checked = draftConfig.floatingDropletsEnabled,
                                        onCheckedChange = { draftConfig = draftConfig.copy(floatingDropletsEnabled = it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = draftConfig.wallpaperType.accentColor,
                                            checkedTrackColor = draftConfig.wallpaperType.accentColor.copy(alpha = 0.35f)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Interactive Touch Reaction Toggle
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Dokunmatik Sıvı Tepkisi",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Ekrana dokunulduğunda organik su dalgalanması",
                                            fontSize = 11.sp,
                                            color = Color(0xFF90A4AE)
                                        )
                                    }
                                    Switch(
                                        checked = draftConfig.interactiveTouchReaction,
                                        onCheckedChange = { draftConfig = draftConfig.copy(interactiveTouchReaction = it) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color(0xFF00F0FF),
                                            checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // 4. Android System Theme & Wallpaper Integration Card
                    item {
                        LiquidGlassCard(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            blurRefractionAlpha = 0.28f,
                            glowAccentColor = Color(0xFF00F0FF)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF00F0FF).copy(alpha = 0.25f))
                                            .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.PhoneAndroid,
                                            contentDescription = "System Theme Integration",
                                            tint = Color(0xFF00F0FF),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "Android Sistem Teması Entegrasyonu",
                                            fontSize = 14.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = "Sıvı camı tüm cihazınızın sistemine uygulayın",
                                            fontSize = 11.sp,
                                            color = Color(0xFF80DEEA)
                                        )
                                    }
                                }

                                if (systemStatusMessage != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color(0xFF00F0FF).copy(alpha = 0.15f))
                                            .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = systemStatusMessage ?: "",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Quick System Wallpaper Target Buttons
                                Text(
                                    text = "Sistem Duvar Kağıdı Hedefi:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF80DEEA)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Home Screen Only
                                    Button(
                                        onClick = {
                                            onApplyAsSystemWallpaper(draftConfig, SystemWallpaperTarget.HOME_SCREEN)
                                            systemStatusMessage = "✓ Ana ekran sistem duvar kağıdı güncellendi!"
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White.copy(alpha = 0.10f),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.Home,
                                                contentDescription = "Ana Ekran",
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Ana Ekran", fontSize = 11.5.sp)
                                        }
                                    }

                                    // Lock Screen Only
                                    Button(
                                        onClick = {
                                            onApplyAsSystemWallpaper(draftConfig, SystemWallpaperTarget.LOCK_SCREEN)
                                            systemStatusMessage = "✓ Kilit ekranı sistem duvar kağıdı güncellendi!"
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White.copy(alpha = 0.10f),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.Lock,
                                                contentDescription = "Kilit Ekranı",
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Kilit", fontSize = 11.5.sp)
                                        }
                                    }

                                    // Both Screens
                                    Button(
                                        onClick = {
                                            onApplyAsSystemWallpaper(draftConfig, SystemWallpaperTarget.BOTH)
                                            systemStatusMessage = "✓ Tüm sisteme (Ana + Kilit) uygulandı!"
                                        },
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF00F0FF).copy(alpha = 0.25f),
                                            contentColor = Color(0xFF00F0FF)
                                        )
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Rounded.Devices,
                                                contentDescription = "Her İkisi",
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "Her İkisi", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Live Animated Wallpaper Service Button
                                Button(
                                    onClick = {
                                        onLaunchLiveWallpaper(draftConfig)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF00E5FF).copy(alpha = 0.15f),
                                        contentColor = Color(0xFF00F0FF)
                                    )
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Waves,
                                            contentDescription = "Live Wallpaper",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Canlı Duvar Kağıdı (Animasyonlu) Olarak Ayarla",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Default Launcher System Theme Button
                                Button(
                                    onClick = {
                                        onSetDefaultLauncher()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(42.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White.copy(alpha = 0.08f),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Home,
                                            contentDescription = "Default Launcher",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Tvnah'ı Varsayılan Sistem Başlatıcısı Yap",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }

                // Bottom Action Bar: Randomize, Reset, Apply Launcher, Apply System Theme
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Randomize Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                                .clickable {
                                    draftConfig = LiquidWallpaperConfig.generateRandomOrganic()
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Shuffle,
                                contentDescription = "Randomize",
                                tint = Color(0xFF80DEEA),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Reset Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White.copy(alpha = 0.08f))
                                .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                                .clickable {
                                    draftConfig = LiquidWallpaperConfig.defaultFor(draftConfig.wallpaperType)
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Refresh,
                                contentDescription = "Reset to default",
                                tint = Color(0xFF80DEEA),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Apply to Launcher Button
                        Button(
                            onClick = {
                                onApplyConfig(draftConfig)
                                appliedLauncherNotification = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.14f),
                                contentColor = Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (appliedLauncherNotification) Icons.Rounded.Check else Icons.Rounded.Wallpaper,
                                    contentDescription = "Apply to Launcher",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (appliedLauncherNotification) "Uygulandı" else "Başlatıcıda",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Apply to System Theme & Wallpaper (High-Priority Bright Glowing Button)
                        Button(
                            onClick = {
                                // Apply to both Launcher AND System Wallpaper with one tap
                                onApplyConfig(draftConfig)
                                onApplyAsSystemWallpaper(draftConfig, SystemWallpaperTarget.BOTH)
                                systemStatusMessage = "✓ Sistem Duvar Kağıdı ve Teması uygulandı!"
                            },
                            modifier = Modifier
                                .weight(1.35f)
                                .height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00F0FF),
                                contentColor = Color(0xFF021020)
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = "Apply as System Theme",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Sistem Teması Yap",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
