package com.example.ui.ios

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.IosWallpaperPreset
import com.example.ui.theme.LauncherFont
import com.example.viewmodel.LauncherUiState

/**
 * Authentic Apple iOS Inset Grouped Settings & Customization Sheet.
 */
@Composable
fun IosSettingsSheet(
    state: LauncherUiState,
    fontFamily: FontFamily,
    context: Context,
    onClose: () -> Unit,
    onSelectIosWallpaper: (IosWallpaperPreset) -> Unit,
    onOpenOnlineWallpapers: () -> Unit,
    onPickGalleryWallpaper: (android.net.Uri) -> Unit,
    onSelectFont: (LauncherFont) -> Unit,
    onToggleClock: () -> Unit,
    onToggleWeather: () -> Unit,
    onToggleBattery: () -> Unit,
    onToggleFavorites: () -> Unit,
    onToggleSearchBar: () -> Unit,
    onToggleFlashlight: () -> Unit,
    onToggleMusicReactive: () -> Unit,
    onSetDefaultLauncher: () -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onPickGalleryWallpaper(uri)
        }
    }

    AnimatedVisibility(
        visible = state.isCustomizeSheetOpen || state.isSettingsOpen,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000).copy(alpha = 0.95f))
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // iOS Navigation Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ayarlar",
                        fontSize = 28.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Apple Done Button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0xFF007AFF))
                            .clickable(onClick = onClose)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Bitti",
                            fontSize = 14.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 1: iOS WALLPAPERS
                IosSectionHeader(title = "DUVAR KAĞIDI", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                // iOS Wallpaper Presets Carousel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IosWallpaperPreset.values().forEach { preset ->
                        val isSelected = state.customGalleryWallpaperUri == null && state.iosWallpaperPreset == preset

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(90.dp)
                                .clickable { onSelectIosWallpaper(preset) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 90.dp, height = 130.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(preset.topColor, preset.midColor, preset.bottomColor)
                                        )
                                    )
                                    .border(
                                        if (isSelected) 2.5.dp else 0.5.dp,
                                        if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.2f),
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF007AFF))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = "Seçili",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = preset.titleTr.replace("iOS ", ""),
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.8f),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Online & Gallery Wallpaper Action Group
                IosGroupCard {
                    IosSettingRow(
                        icon = Icons.Rounded.CloudDownload,
                        title = "Çevrimiçi Duvar Kağıtları (4K / HD)",
                        fontFamily = fontFamily,
                        onClick = onOpenOnlineWallpapers
                    )
                    IosDivider()
                    IosSettingRow(
                        icon = Icons.Rounded.Image,
                        title = "Galeriden Fotoğraf Seç",
                        fontFamily = fontFamily,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 2: FONTS & TYPOGRAPHY
                IosSectionHeader(title = "YAZI TİPİ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    LauncherFont.values().forEachIndexed { index, font ->
                        val isSelected = state.selectedFont == font
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectFont(font) }
                                .padding(horizontal = 16.dp, vertical = 13.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = font.titleTr,
                                fontSize = 15.sp,
                                fontFamily = fontFamily,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = Color.White
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Seçili",
                                    tint = Color(0xFF007AFF),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (index < LauncherFont.values().size - 1) {
                            IosDivider()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 3: HOME SCREEN CONTENT (Modular Widgets Toggle)
                IosSectionHeader(title = "ANA EKRAN BİLEŞENLERİ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    IosToggleRow(
                        icon = Icons.Rounded.Schedule,
                        title = "Büyük Saat & Tarih Başlığı",
                        checked = state.showClockWidget,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleClock() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.WbSunny,
                        title = "Hava Durumu Widget'ı",
                        checked = state.showWeatherWidget,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleWeather() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.BatteryFull,
                        title = "Pil Göstergesi Widget'ı",
                        checked = state.showBatteryWidget,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleBattery() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.Star,
                        title = "Sık Kullanılanlar Satırı",
                        checked = state.showFavoritesShelf,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleFavorites() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.Search,
                        title = "Arama Çubuğu / Düğmesi",
                        checked = state.showSearchBar,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleSearchBar() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.FlashlightOn,
                        title = "Kilit Ekranı Fener Kısayolu",
                        checked = state.showFlashlightQuickAction,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleFlashlight() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.MusicNote,
                        title = "Müziğe Göre Işıma Efekti",
                        checked = state.isMusicReactive,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleMusicReactive() }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 4: SYSTEM INTEGRATION
                IosSectionHeader(title = "SİSTEM", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    IosSettingRow(
                        icon = Icons.Rounded.Home,
                        title = "Varsayılan Başlatıcı Olarak Ayarla",
                        fontFamily = fontFamily,
                        onClick = onSetDefaultLauncher
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun IosSectionHeader(title: String, fontFamily: FontFamily) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.6.sp,
        color = Color.White.copy(alpha = 0.55f),
        modifier = Modifier.padding(start = 12.dp)
    )
}

@Composable
private fun IosGroupCard(content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, shape)
            .clip(shape)
            .background(Color(0xFF1C1C1E))
            .border(0.5.dp, Color.White.copy(alpha = 0.10f), shape)
    ) {
        content()
    }
}

@Composable
private fun IosDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 48.dp)
            .height(0.5.dp)
            .background(Color.White.copy(alpha = 0.12f))
    )
}

@Composable
private fun IosSettingRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF007AFF))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(17.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontFamily = fontFamily,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun IosToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    fontFamily: FontFamily,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF34C759))
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontFamily = fontFamily,
                color = Color.White
            )
        }

        // Apple Green Switch
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF34C759),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF39393D)
            )
        )
    }
}
