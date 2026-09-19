package com.example.ui.ios

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppCategory
import com.example.model.AppInfo
import com.example.viewmodel.LauncherUiState

/**
 * Authentic Apple iOS App Library (Uygulama Arşivi) modal sheet.
 */
@Composable
fun IosAppLibrarySheet(
    state: LauncherUiState,
    fontFamily: FontFamily,
    onClose: () -> Unit,
    onSearchChange: (String) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onToggleDockPin: (AppInfo) -> Unit,
    onOpenAppDetails: (String) -> Unit,
    onUninstallApp: (String) -> Unit,
    onDismissContextMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = state.isDrawerOpen,
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
                            Color(0xF5101012),
                            Color(0xF80B0C0E),
                            Color(0xFF000000)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Header & Search Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // iOS Search Field Container
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Ara",
                                tint = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        text = "Uygulama Arşivi",
                                        color = Color.White.copy(alpha = 0.5f),
                                        fontSize = 15.sp,
                                        fontFamily = fontFamily
                                    )
                                }
                                BasicTextField(
                                    value = state.searchQuery,
                                    onValueChange = onSearchChange,
                                    textStyle = TextStyle(
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontFamily = fontFamily
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF007AFF)),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchChange("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "Temizle",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Cancel / Close
                    Text(
                        text = "Vazgeç",
                        color = Color(0xFF007AFF),
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onClose)
                            .padding(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content: Either Search Results or Categorized iOS Folder Grid
                if (state.searchQuery.isNotEmpty()) {
                    // Search List Mode
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(state.filteredApps, key = { it.packageName }) { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(Color.White.copy(alpha = 0.06f))
                                    .clickable { onAppClick(app) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IosAppIcon(
                                    app = app,
                                    iconSize = 44.dp,
                                    fontFamily = fontFamily,
                                    showLabel = false,
                                    onClick = { onAppClick(app) },
                                    onLongClick = { onAppLongClick(app) }
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = app.label,
                                    fontSize = 15.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // Categorized iOS Folders (2x2 Folder Blocks)
                    val categories = listOf(
                        AppCategory.SOCIAL to "Sosyal",
                        AppCategory.MEDIA to "Eğlence & Medya",
                        AppCategory.TOOLS to "Yardımcı Araçlar",
                        AppCategory.ESSENTIALS to "Üretkenlik",
                        AppCategory.GAMES to "Oyunlar",
                        AppCategory.ALL to "Tüm Uygulamalar"
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(categories) { (cat, title) ->
                            val appsInCat = if (cat == AppCategory.ALL) state.allApps
                            else state.allApps.filter { it.category == cat }

                            IosFolderCard(
                                title = title,
                                apps = appsInCat,
                                fontFamily = fontFamily,
                                onAppClick = onAppClick,
                                onAppLongClick = onAppLongClick
                            )
                        }
                    }
                }
            }
        }
    }

    // iOS Context Menu (3D Touch popup)
    if (state.activeContextMenuApp != null) {
        val app = state.activeContextMenuApp
        IosContextMenuDialog(
            app = app,
            fontFamily = fontFamily,
            isPinnedToDock = state.dockApps.any { it.packageName == app.packageName },
            onDismiss = onDismissContextMenu,
            onLaunch = { onAppClick(app); onDismissContextMenu() },
            onToggleDock = { onToggleDockPin(app); onDismissContextMenu() },
            onAppDetails = { onOpenAppDetails(app.packageName); onDismissContextMenu() },
            onUninstall = { onUninstallApp(app.packageName); onDismissContextMenu() }
        )
    }
}

/**
 * 2x2 App Folder in iOS App Library.
 */
@Composable
private fun IosFolderCard(
    title: String,
    apps: List<AppInfo>,
    fontFamily: FontFamily,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit
) {
    val folderShape = RoundedCornerShape(26.dp)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(155.dp)
                .shadow(8.dp, folderShape, ambientColor = Color.Black.copy(alpha = 0.5f))
                .clip(folderShape)
                .background(Color.White.copy(alpha = 0.12f))
                .border(0.5.dp, Color.White.copy(alpha = 0.15f), folderShape)
                .padding(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = false
            ) {
                items(apps.take(4)) { app ->
                    IosAppIcon(
                        app = app,
                        iconSize = 48.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(app) },
                        onLongClick = { onAppLongClick(app) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

/**
 * Authentic Apple iOS Context Menu Popup.
 */
@Composable
private fun IosContextMenuDialog(
    app: AppInfo,
    fontFamily: FontFamily,
    isPinnedToDock: Boolean,
    onDismiss: () -> Unit,
    onLaunch: () -> Unit,
    onToggleDock: () -> Unit,
    onAppDetails: () -> Unit,
    onUninstall: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        val menuShape = RoundedCornerShape(18.dp)

        Box(
            modifier = Modifier
                .width(260.dp)
                .shadow(24.dp, menuShape, ambientColor = Color.Black.copy(alpha = 0.8f))
                .clip(menuShape)
                .background(Color(0xF0252528))
                .border(0.5.dp, Color.White.copy(alpha = 0.18f), menuShape)
        ) {
            Column {
                // App Header in popup
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IosAppIcon(
                        app = app,
                        iconSize = 40.dp,
                        fontFamily = fontFamily,
                        showLabel = false
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = app.label,
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(Color.White.copy(alpha = 0.12f))
                )

                // Item 1: Open
                IosContextMenuItem(
                    icon = Icons.Rounded.Launch,
                    title = "Uygulamayı Aç",
                    fontFamily = fontFamily,
                    onClick = onLaunch
                )

                // Item 2: Pin to Dock
                IosContextMenuItem(
                    icon = Icons.Rounded.PushPin,
                    title = if (isPinnedToDock) "Dock'tan Çıkar" else "Dock'a Sabitle",
                    fontFamily = fontFamily,
                    onClick = onToggleDock
                )

                // Item 3: Info
                IosContextMenuItem(
                    icon = Icons.Rounded.Info,
                    title = "Uygulama Bilgileri",
                    fontFamily = fontFamily,
                    onClick = onAppDetails
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(Color.White.copy(alpha = 0.12f))
                )

                // Item 4: Delete App (iOS Destructive Red)
                IosContextMenuItem(
                    icon = Icons.Rounded.DeleteForever,
                    title = "Uygulamayı Sil",
                    fontFamily = fontFamily,
                    color = Color(0xFFFF3B30),
                    onClick = onUninstall
                )
            }
        }
    }
}

@Composable
private fun IosContextMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    fontFamily: FontFamily,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.5.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            color = color
        )
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
    }
}
