package com.example.ui.ios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppCategory
import com.example.model.AppInfo
import com.example.model.FolderConfig
import com.example.model.FolderGridColumns
import com.example.model.FolderShape

/**
 * Returns a shape based on FolderShape enum.
 */
fun getFolderCornerShape(folderShape: FolderShape): RoundedCornerShape {
    return when (folderShape) {
        FolderShape.SQUIRCLE -> RoundedCornerShape(22.dp)
        FolderShape.PILL -> RoundedCornerShape(32.dp)
        FolderShape.SHARP -> RoundedCornerShape(10.dp)
        FolderShape.CIRCLE -> RoundedCornerShape(42.dp)
    }
}

/**
 * Expanded Folder Modal Dialog:
 * Opens when a user taps on any Category Folder in the App Library.
 * Displays all apps within that folder using customizable grid columns (2, 3, or 4 columns),
 * customizable folder shape, and adjustable opacity.
 */
@Composable
fun ExpandedFolderDialog(
    category: AppCategory,
    categoryTitle: String,
    apps: List<AppInfo>,
    folderConfig: FolderConfig,
    fontFamily: FontFamily,
    onDismiss: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onOpenFolderSettings: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val cornerShape = getFolderCornerShape(folderConfig.shape)
        val backgroundColor = Color(0xFF181A1E).copy(alpha = folderConfig.opacity)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .height(520.dp)
                    .shadow(28.dp, cornerShape)
                    .clip(cornerShape)
                    .background(backgroundColor)
                    .border(
                        1.dp,
                        Color.White.copy(alpha = 0.18f),
                        cornerShape
                    )
                    .clickable(enabled = false) {} // Prevent click-through
                    .padding(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Folder Header: Category Title, App Count, Settings & Close Icon
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = categoryTitle,
                                fontSize = 22.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${apps.size} uygulama • ${folderConfig.gridColumns.titleTr}",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.65f)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Quick button to open Folder Customization sheet
                            IconButton(
                                onClick = onOpenFolderSettings,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.12f))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Tune,
                                    contentDescription = "Klasör Ayarları",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.12f))
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Kapat",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Inner App Grid with user-chosen column count (2, 3, or 4)
                    if (apps.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Bu klasörde henüz uygulama yok",
                                fontSize = 14.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(folderConfig.gridColumns.columns),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(apps, key = { it.packageName }) { app ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IosAppIcon(
                                        app = app,
                                        iconSize = if (folderConfig.gridColumns == FolderGridColumns.TWO) 64.dp else 52.dp,
                                        fontFamily = fontFamily,
                                        showLabel = true,
                                        onClick = { onAppClick(app) },
                                        onLongClick = { onAppLongClick(app) }
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

/**
 * Dedicated Sheet / Dialog to customize Folder Shape, Opacity (Transparency), and Grid Columns.
 */
@Composable
fun FolderConfigSheet(
    isOpen: Boolean,
    folderConfig: FolderConfig,
    fontFamily: FontFamily,
    onClose: () -> Unit,
    onShapeChange: (FolderShape) -> Unit,
    onOpacityChange: (Float) -> Unit,
    onGridColumnsChange: (FolderGridColumns) -> Unit
) {
    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF101114))
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF007AFF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Klasör Özelleştirme",
                                fontSize = 18.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Şekil, şeffaflık ve ızgara düzeni",
                                fontSize = 12.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF26282E))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Kapat",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 1. LIVE PREVIEW CARD
                Text(
                    text = "CANLI ÖNİZLEME",
                    fontSize = 11.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                val previewShape = getFolderCornerShape(folderConfig.shape)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF1A1C22))
                        .border(0.5.dp, Color.White.copy(alpha = 0.10f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // The stylized folder preview box
                    Box(
                        modifier = Modifier
                            .size(width = 150.dp, height = 110.dp)
                            .shadow(12.dp, previewShape)
                            .clip(previewShape)
                            .background(Color(0xFF242730).copy(alpha = folderConfig.opacity))
                            .border(1.dp, Color.White.copy(alpha = 0.25f), previewShape)
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Folder,
                                contentDescription = null,
                                tint = Color(0xFF007AFF),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = folderConfig.shape.titleTr,
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Şeffaflık: %${(folderConfig.opacity * 100).toInt()}",
                                fontSize = 10.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 2. FOLDER SHAPE
                Text(
                    text = "KLASÖR KÖŞE ŞEKLİ",
                    fontSize = 11.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FolderShape.values().forEach { shape ->
                        val isSelected = folderConfig.shape == shape
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF1E2026))
                                .border(
                                    0.5.dp,
                                    if (isSelected) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onShapeChange(shape) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
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

                Spacer(modifier = Modifier.height(24.dp))

                // 3. FOLDER OPACITY / TRANSPARENCY SLIDER
                Text(
                    text = "KLASÖR ŞEFFAFLIK / MATLIK AYARI",
                    fontSize = 11.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF1E2026))
                        .border(0.5.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Opacity,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Yüzey Opaklığı",
                                    fontSize = 13.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "%${(folderConfig.opacity * 100).toInt()}",
                                fontSize = 13.sp,
                                fontFamily = fontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF007AFF)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Slider(
                            value = folderConfig.opacity,
                            onValueChange = onOpacityChange,
                            valueRange = 0.20f..1.0f,
                            steps = 15,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF007AFF),
                                activeTrackColor = Color(0xFF007AFF),
                                inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Şeffaf Cam (%20)",
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.45f)
                            )
                            Text(
                                text = "Mat Katı (%100)",
                                fontSize = 11.sp,
                                fontFamily = fontFamily,
                                color = Color.White.copy(alpha = 0.45f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 4. INNER FOLDER GRID OPTIONS (Columns: 2, 3, 4)
                Text(
                    text = "KLASÖR İÇİ IZGARA DÜZENİ (GRID)",
                    fontSize = 11.5.sp,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FolderGridColumns.values().forEach { colOption ->
                        val isSelected = folderConfig.gridColumns == colOption
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFF007AFF) else Color(0xFF1E2026))
                                .border(
                                    0.5.dp,
                                    if (isSelected) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable { onGridColumnsChange(colOption) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.GridView,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = colOption.titleTr,
                                    fontSize = 12.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
