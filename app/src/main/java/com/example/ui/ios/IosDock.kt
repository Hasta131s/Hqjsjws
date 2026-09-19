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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.model.AppInfo

/**
 * Clean Minimalist Bottom Dock (Defaults to 2 apps, avoiding overcrowding).
 * Solid matte surface, no glass glare.
 */
@Composable
fun IosDock(
    dockApps: List<AppInfo>,
    dockLimit: Int = 2,
    fontFamily: FontFamily,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onOpenAppLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dockShape = RoundedCornerShape(32.dp)
    val displayApps = dockApps.take(dockLimit)

    Box(
        modifier = modifier
            .widthIn(min = 140.dp, max = 280.dp)
            .shadow(12.dp, dockShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(dockShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.16f), dockShape)
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
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(16.dp))
                        .clickable(onClick = onOpenAppLibrary)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Uygulama Ekle",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
