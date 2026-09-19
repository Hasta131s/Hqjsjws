package com.example.ui.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.model.AppInfo
import com.example.model.LauncherThemeMode
import com.example.ui.theme.rememberColorOsPalette

/**
 * ColorOS Clean Minimalist Frosted Bottom Dock.
 * Features customizable blur intensity, surface opacity, and Dark/Light theme switching.
 */
@Composable
fun IosDock(
    dockApps: List<AppInfo>,
    dockLimit: Int = 2,
    fontFamily: FontFamily,
    themeMode: LauncherThemeMode = LauncherThemeMode.DARK_AMOLED,
    surfaceOpacity: Float = 0.55f,
    blurRadiusDp: Float = 24f,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onOpenAppLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = rememberColorOsPalette(themeMode = themeMode, surfaceOpacity = surfaceOpacity)
    val dockShape = RoundedCornerShape(32.dp)
    val displayApps = dockApps.take(dockLimit)

    val shadowElevation = if (blurRadiusDp > 0f) (blurRadiusDp * 0.5f).coerceIn(6f, 20f).dp else 0.dp

    Box(
        modifier = modifier
            .widthIn(min = 140.dp, max = 280.dp)
            .shadow(
                elevation = shadowElevation,
                shape = dockShape,
                ambientColor = if (palette.isDark) Color.Black.copy(alpha = 0.6f) else Color(0x35000000),
                spotColor = if (palette.isDark) Color.Black.copy(alpha = 0.45f) else Color(0x25000000)
            )
            .clip(dockShape)
            .background(
                Brush.verticalGradient(
                    colors = if (palette.isDark) listOf(
                        Color(0xFF1B1E26).copy(alpha = surfaceOpacity.coerceIn(0.25f, 0.92f)),
                        Color(0xFF0F1116).copy(alpha = (surfaceOpacity + 0.12f).coerceIn(0.30f, 0.98f))
                    ) else listOf(
                        Color(0xFFFFFFFF).copy(alpha = surfaceOpacity.coerceIn(0.50f, 0.95f)),
                        Color(0xFFE2E8F0).copy(alpha = (surfaceOpacity + 0.15f).coerceIn(0.60f, 0.98f))
                    )
                )
            )
            .border(
                0.75.dp,
                if (palette.isDark) Color.White.copy(alpha = (surfaceOpacity * 0.35f).coerceIn(0.10f, 0.30f))
                else Color.Black.copy(alpha = 0.08f),
                dockShape
            )
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayApps.forEach { app ->
                IosAppIcon(
                    app = app,
                    iconSize = 56.dp,
                    fontFamily = fontFamily,
                    showLabel = false,
                    onClick = { onAppClick(app) },
                    onLongClick = { onAppLongClick(app) }
                )
            }

            // If dock has fewer than limit, show a clean "+" slot to add apps from library
            if (displayApps.size < dockLimit) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (palette.isDark) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.05f))
                        .border(
                            1.dp,
                            if (palette.isDark) Color.White.copy(alpha = 0.18f) else Color.Black.copy(alpha = 0.10f),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = onOpenAppLibrary)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Uygulama Ekle",
                        tint = palette.primaryTextColor.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
