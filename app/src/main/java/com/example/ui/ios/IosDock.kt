package com.example.ui.ios

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
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
 * Authentic Apple iOS Frosted Bottom Dock.
 */
@Composable
fun IosDock(
    dockApps: List<AppInfo>,
    fontFamily: FontFamily,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onOpenAppLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dockShape = RoundedCornerShape(35.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(16.dp, dockShape, ambientColor = Color.Black.copy(alpha = 0.6f))
            .clip(dockShape)
            .background(Color.White.copy(alpha = 0.22f))
            .border(0.5.dp, Color.White.copy(alpha = 0.35f), dockShape)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pinned dock apps (up to 4)
            dockApps.take(4).forEach { app ->
                IosAppIcon(
                    app = app,
                    iconSize = 54.dp,
                    fontFamily = fontFamily,
                    showLabel = false,
                    onClick = { onAppClick(app) },
                    onLongClick = { onAppLongClick(app) }
                )
            }

            // App Library trigger icon in dock
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black.copy(alpha = 0.25f))
                    .border(0.5.dp, Color.White.copy(alpha = 0.20f), RoundedCornerShape(14.dp))
                    .clickable(onClick = onOpenAppLibrary)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Apps,
                    contentDescription = "Uygulama Arşivi",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
