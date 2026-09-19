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
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.BatteryFull
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.ViewColumn
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import com.example.model.FolderGridColumns
import com.example.model.FolderShape
import com.example.model.IosWallpaperPreset
import com.example.model.WidgetShape
import com.example.ui.theme.ClockFontOption
import com.example.ui.theme.LauncherFont
import com.example.ui.theme.getClockFontFamily
import com.example.ui.theme.getClockFontWeight
import com.example.viewmodel.LauncherUiState

/**
 * Modern Inset Grouped Settings & Full Customization Sheet.
 * Features:
 * - 20 Selectable Clock Fonts
 * - Clock Size Slider
 * - Widget Scale & Shape Selector
 * - Dock App Limit Selector (Default 2 for ultra clean dock)
 * - Grid Columns Selector
 * - Solid Matte Cards (No glass glare, no brand mentions)
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
    onSelectClockFont: (ClockFontOption) -> Unit,
    onChangeClockSize: (Float) -> Unit,
    onChangeWidgetScale: (Float) -> Unit,
    onChangeWidgetShape: (WidgetShape) -> Unit = {},
    onChangeDockLimit: (Int) -> Unit,
    onChangeGridColumns: (Int) -> Unit,
    onChangeFolderShape: (FolderShape) -> Unit = {},
    onChangeFolderOpacity: (Float) -> Unit = {},
    onChangeFolderGridColumns: (FolderGridColumns) -> Unit = {},
    onOpenFolderConfig: () -> Unit = {},
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
                .background(Color(0xFF101012))
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Özelleştirme & Ayarlar",
                        fontSize = 24.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Done Button
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

                // SECTION 1: 20 CLOCK FONTS (Requested explicitly)
                IosSectionHeader(title = "SAAT YAZI TİPİ (20 FARKLI FONT)", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    ClockFontOption.values().forEachIndexed { index, option ->
                        val isSelected = state.clockFontOption == option
                        val optionFamily = getClockFontFamily(option)
                        val optionWeight = getClockFontWeight(option)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectClockFont(option) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = option.displayName,
                                    fontSize = 14.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                                Text(
                                    text = "14:52",
                                    fontSize = 18.sp,
                                    fontFamily = optionFamily,
                                    fontWeight = optionWeight,
                                    color = if (isSelected) Color(0xFF007AFF) else Color.White.copy(alpha = 0.65f)
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Seçili",
                                    tint = Color(0xFF007AFF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (index < ClockFontOption.values().size - 1) {
                            IosDivider()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 2: CLOCK SIZE CUSTOMIZATION
                IosSectionHeader(title = "SAAT BOYUTU (${state.clockSizeSp.toInt()} SP)", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Küçük (48)",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Standart (68)",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Büyük (96)",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                        Slider(
                            value = state.clockSizeSp,
                            onValueChange = { onChangeClockSize(it) },
                            valueRange = 40f..100f,
                            steps = 12,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF007AFF),
                                activeTrackColor = Color(0xFF007AFF),
                                inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 3: WIDGET SCALE & SHAPE CUSTOMIZATION
                IosSectionHeader(title = "WİDGET BOYUTLARI VE ŞEKLİ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Boyut",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                0.85f to "Kompakt (%85)",
                                1.00f to "Standart (%100)",
                                1.15f to "Geniş (%115)"
                            ).forEach { (scaleVal, label) ->
                                val isSelected = kotlin.math.abs(state.widgetScale - scaleVal) < 0.05f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                        .clickable { onChangeWidgetScale(scaleVal) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontFamily = fontFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Bileşen Köşe Şekli",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            WidgetShape.values().forEach { shape ->
                                val isSelected = state.widgetShape == shape
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                        .clickable { onChangeWidgetShape(shape) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = shape.titleTr,
                                        fontSize = 11.sp,
                                        fontFamily = fontFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 4: DOCK APP LIMIT (User requested: "alttaki 2 den fazla şey olunca fazlalık kötü gözüküyor")
                IosSectionHeader(title = "ALT DOCK UYGULAMA SAYISI", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            2 to "2 Uygulama (Sade)",
                            3 to "3 Uygulama",
                            4 to "4 Uygulama"
                        ).forEach { (count, label) ->
                            val isSelected = state.dockAppLimit == count
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                    .clickable { onChangeDockLimit(count) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 5: GRID COLUMNS
                IosSectionHeader(title = "UYGULAMA IZGARASI SÜTUN SAYISI", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3 to "3 Sütun", 4 to "4 Sütun (Standart)", 5 to "5 Sütun").forEach { (cols, label) ->
                            val isSelected = state.gridColumns == cols
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                    .clickable { onChangeGridColumns(cols) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 5.1: FOLDER CUSTOMIZATION (Shape, Opacity & Grid)
                IosSectionHeader(title = "UYGULAMA KLASÖRLERİ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

                IosGroupCard {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Folder Shape
                        Text(
                            text = "Klasör Köşe Şekli",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FolderShape.values().forEach { shape ->
                                val isSelected = state.folderConfig.shape == shape
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                        .clickable { onChangeFolderShape(shape) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = shape.titleTr,
                                        fontSize = 10.5.sp,
                                        fontFamily = fontFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Folder Opacity Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Klasör Şeffaflığı / Opaklığı",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "%${(state.folderConfig.opacity * 100).toInt()}",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF007AFF)
                            )
                        }

                        Slider(
                            value = state.folderConfig.opacity,
                            onValueChange = onChangeFolderOpacity,
                            valueRange = 0.20f..1.0f,
                            steps = 15,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF007AFF),
                                activeTrackColor = Color(0xFF007AFF),
                                inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Folder Inner Grid Columns
                        Text(
                            text = "Klasör İçi Izgara Düzeni",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FolderGridColumns.values().forEach { colOption ->
                                val isSelected = state.folderConfig.gridColumns == colOption
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF26282E))
                                        .clickable { onChangeFolderGridColumns(colOption) }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = colOption.titleTr,
                                        fontSize = 11.sp,
                                        fontFamily = fontFamily,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 6: WALLPAPERS
                IosSectionHeader(title = "DUVAR KAĞIDI PRESETLERİ", fontFamily = fontFamily)
                Spacer(modifier = Modifier.height(6.dp))

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
                                    .size(width = 90.dp, height = 125.dp)
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
                                text = preset.titleTr,
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

                IosGroupCard {
                    IosSettingRow(
                        icon = Icons.Rounded.CloudDownload,
                        title = "Çevrimiçi 4K Duvar Kağıtları İndir",
                        fontFamily = fontFamily,
                        onClick = onOpenOnlineWallpapers
                    )
                    IosDivider()
                    IosSettingRow(
                        icon = Icons.Rounded.Image,
                        title = "Galeriden Duvar Kağıdı Seç",
                        fontFamily = fontFamily,
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 7: HOME SCREEN CONTENT TOGGLES
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
                        title = "Fener Kısayolu",
                        checked = state.showFlashlightQuickAction,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleFlashlight() }
                    )
                    IosDivider()
                    IosToggleRow(
                        icon = Icons.Rounded.MusicNote,
                        title = "Müziğe Göre Işıma",
                        checked = state.isMusicReactive,
                        fontFamily = fontFamily,
                        onCheckedChange = { onToggleMusicReactive() }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // SECTION 8: SYSTEM LAUNCHER
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
        fontSize = 11.5.sp,
        fontFamily = fontFamily,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp,
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
            .background(Color(0xFF1C1D21))
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
            .background(Color.White.copy(alpha = 0.10f))
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
            fontSize = 14.5.sp,
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
            .padding(horizontal = 14.dp, vertical = 8.dp),
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
                fontSize = 14.5.sp,
                fontFamily = fontFamily,
                color = Color.White
            )
        }

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
