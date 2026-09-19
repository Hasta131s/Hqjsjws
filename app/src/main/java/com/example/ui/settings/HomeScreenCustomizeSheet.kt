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
import androidx.compose.material.icons.rounded.BatteryChargingFull
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashlightOn
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.liquid.LiquidGlassCard
import com.example.ui.theme.LauncherFont
import com.example.viewmodel.LauncherUiState

@Composable
fun HomeScreenCustomizeSheet(
    state: LauncherUiState,
    onClose: () -> Unit,
    onToggleClock: () -> Unit,
    onToggleWeather: () -> Unit,
    onToggleBattery: () -> Unit,
    onToggleMedia: () -> Unit,
    onToggleFlashlight: () -> Unit,
    onToggleFavorites: () -> Unit,
    onToggleSearchBar: () -> Unit,
    onToggleMusicReactive: () -> Unit,
    onSelectFont: (LauncherFont) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.isCustomizeSheetOpen,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xF206101E),
                            Color(0xFA040C16),
                            Color(0xFF02070D)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
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
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF00F0FF).copy(alpha = 0.20f))
                                .border(1.dp, Color(0xFF00F0FF).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Özelleştir",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Ana Ekranı Özelleştir",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Menü içeriklerini dilediğince ekle veya kaldır",
                                fontSize = 12.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Kapat",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category 1: Audio Reactive Mode
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.25f,
                        glowAccentColor = Color(0xFF00F0FF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF00F0FF).copy(alpha = 0.2f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.GraphicEq,
                                        contentDescription = "Ritim",
                                        tint = Color(0xFF00F0FF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Müziğe Göre Hareket & Ritim",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Sıvı dalgalar çalan ritimle organik genişlesin",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF80DEEA)
                                    )
                                }
                            }

                            Switch(
                                checked = state.isMusicReactive,
                                onCheckedChange = { onToggleMusicReactive() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF00F0FF),
                                    checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f),
                                    uncheckedThumbColor = Color.LightGray,
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.12f)
                                )
                            )
                        }
                    }

                    // Category 2: Typography & Font
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.20f
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.TextFields,
                                    contentDescription = "Yazı Tipi",
                                    tint = Color(0xFF00F0FF),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Tipografi & Yazı Tipi",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                LauncherFont.values().forEach { font ->
                                    val isSelected = state.selectedFont == font
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.22f)
                                                else Color.White.copy(alpha = 0.05f)
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.6f)
                                                else Color.White.copy(alpha = 0.08f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable { onSelectFont(font) }
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = font.titleTr,
                                            fontSize = 13.sp,
                                            color = if (isSelected) Color.White else Color(0xFFB0BEC5),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = "Seçildi",
                                                tint = Color(0xFF00F0FF),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Category 3: Widgets Visibility Toggles
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.20f
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Ana Ekran Bileşenleri (Aç/Kapat)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            CustomizeToggleRow(
                                icon = Icons.Rounded.Schedule,
                                title = "Saat & Tarih Başlığı",
                                subtitle = "Minimal iOS dijital saat gösterimi",
                                checked = state.showClockWidget,
                                onToggle = onToggleClock
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.FlashlightOn,
                                title = "Fener Kısayolu",
                                subtitle = "Cihazın kamerasındaki gerçek flaşı açar",
                                checked = state.showFlashlightQuickAction,
                                onToggle = onToggleFlashlight
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.WbSunny,
                                title = "Hava Durumu Kapsülü",
                                subtitle = "Sıcaklık ve atmosferik durum",
                                checked = state.showWeatherWidget,
                                onToggle = onToggleWeather
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.BatteryChargingFull,
                                title = "Batarya & Enerji Kapsülü",
                                subtitle = "Pil seviyesi ve hızlı Eco/120Hz butonu",
                                checked = state.showBatteryWidget,
                                onToggle = onToggleBattery
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.MusicNote,
                                title = "Medya & Müzik Oynatıcı",
                                subtitle = "Şarkı bilgisi ve hızlı parça kontrolleri",
                                checked = state.showMediaWidget,
                                onToggle = onToggleMedia
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.Star,
                                title = "Favori Uygulamalar Rafı",
                                subtitle = "Sık kullanılan uygulamaların mini dock'u",
                                checked = state.showFavoritesShelf,
                                onToggle = onToggleFavorites
                            )

                            CustomizeToggleRow(
                                icon = Icons.Rounded.Search,
                                title = "Arama Çubuğu",
                                subtitle = "Uygulama ve internette hızlı arama",
                                checked = state.showSearchBar,
                                onToggle = onToggleSearchBar
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun CustomizeToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (checked) Color(0xFF00F0FF) else Color(0xFF607D8B),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF90A4AE)
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF00F0FF),
                checkedTrackColor = Color(0xFF00F0FF).copy(alpha = 0.35f),
                uncheckedThumbColor = Color.LightGray,
                uncheckedTrackColor = Color.White.copy(alpha = 0.12f)
            )
        )
    }
}
