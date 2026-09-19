package com.example.ui.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DashboardCustomize
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Menu sheet triggered by long-pressing an empty space on the home screen.
 * Provides quick access to Add Widgets, Change Wallpaper, Reorder/Edit icons, and Settings.
 */
@Composable
fun HomeActionMenuSheet(
    fontFamily: FontFamily,
    onAddWidget: () -> Unit,
    onChangeWallpaper: () -> Unit,
    onEditIcons: () -> Unit,
    onOpenSettings: () -> Unit,
    onSetDefaultLauncher: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(26.dp))
                .background(Color(0xFF1B1C20))
                .border(0.5.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(26.dp))
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
                            text = "Ana Ekranı Özelleştir",
                            fontSize = 18.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Widget ekle, duvar kağıdı seç veya simgeleri düzenle",
                            fontSize = 11.5.sp,
                            fontFamily = fontFamily,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Rounded.Close, contentDescription = "Kapat", tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 1. WIDGET EKLE
                HomeActionMenuItem(
                    icon = Icons.Rounded.Widgets,
                    title = "Widget Ekle",
                    subtitle = "+6 farklı zengin bileşenden dilediğini seç",
                    accentColor = Color(0xFF007AFF),
                    fontFamily = fontFamily,
                    onClick = {
                        onDismiss()
                        onAddWidget()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 2. DUVAR KAĞIDI DEĞİŞTİR
                HomeActionMenuItem(
                    icon = Icons.Rounded.Wallpaper,
                    title = "Duvar Kağıdı Değiştir",
                    subtitle = "Online HD koleksiyonu veya galeri fotoğrafı seç",
                    accentColor = Color(0xFFFF9500),
                    fontFamily = fontFamily,
                    onClick = {
                        onDismiss()
                        onChangeWallpaper()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 3. SİMGELERİ DÜZENLE / SIRALA
                HomeActionMenuItem(
                    icon = Icons.Rounded.DashboardCustomize,
                    title = "Uygulama Simgelerini Düzenle",
                    subtitle = "Konumları değiştir, sırala veya ana ekrandan kaldır",
                    accentColor = Color(0xFF34C759),
                    fontFamily = fontFamily,
                    onClick = {
                        onDismiss()
                        onEditIcons()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 4. BAŞLATICIYI VARSAYILAN YAP
                HomeActionMenuItem(
                    icon = Icons.Rounded.Home,
                    title = "Varsayılan Başlatıcı Olarak Ayarla",
                    subtitle = "Tvnah Launcher'ı telefonun varsayılan ana ekranı yap",
                    accentColor = Color(0xFFAF52DE),
                    fontFamily = fontFamily,
                    onClick = {
                        onDismiss()
                        onSetDefaultLauncher()
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 5. TÜM AYARLAR
                HomeActionMenuItem(
                    icon = Icons.Rounded.Settings,
                    title = "Başlatıcı Ayarları",
                    subtitle = "Yazı tipleri, saat boyutu, dock ve görünüm seçenekleri",
                    accentColor = Color(0xFF8E8E93),
                    fontFamily = fontFamily,
                    onClick = {
                        onDismiss()
                        onOpenSettings()
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeActionMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    fontFamily: FontFamily,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentColor.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontSize = 14.sp, fontFamily = fontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 11.sp, fontFamily = fontFamily, color = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}
