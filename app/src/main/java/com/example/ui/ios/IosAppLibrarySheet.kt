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
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
 * Clean Modern App Library (Uygulama Arşivi) modal sheet.
 * Fully solid matte surfaces, no glass glare, no brand references.
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
    onToggleFavorite: (AppInfo) -> Unit,
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
                .background(Color(0xFF121214))
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
                    // Modern Search Field Container
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF222226))
                            .border(0.5.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
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
                        text = "Kapat",
                        color = Color(0xFF007AFF),
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onClose)
                            .padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content: If searching, show list; otherwise show 2x2 Category Folders
                if (state.searchQuery.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(state.filteredApps, key = { it.packageName }) { app ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onAppClick(app) }
                                    .padding(horizontal = 8.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IosAppIcon(
                                    app = app,
                                    iconSize = 46.dp,
                                    fontFamily = fontFamily,
                                    showLabel = false,
                                    onLongClick = { onAppLongClick(app) }
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = app.label,
                                        fontSize = 16.sp,
                                        fontFamily = fontFamily,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = app.category.titleTr,
                                        fontSize = 12.sp,
                                        fontFamily = fontFamily,
                                        color = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Category Folders Grid
                    val categoriesWithApps = listOf(
                        AppCategory.ESSENTIALS to "Önemli",
                        AppCategory.SOCIAL to "Sosyal",
                        AppCategory.MEDIA to "Medya & Eğlence",
                        AppCategory.TOOLS to "Araçlar",
                        AppCategory.GAMES to "Oyunlar",
                        AppCategory.SYSTEM to "Sistem"
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 20.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(categoriesWithApps, key = { it.first.name }) { pair ->
                            val appsInCat = state.allApps.filter { it.category == pair.first }
                            IosCategoryFolderCard(
                                title = pair.second,
                                apps = appsInCat,
                                fontFamily = fontFamily,
                                onAppClick = onAppClick,
                                onAppLongClick = onAppLongClick
                            )
                        }

                        // Sık Kullanılanlar / Favoriler Kartı
                        if (state.favoriteApps.isNotEmpty()) {
                            item {
                                IosCategoryFolderCard(
                                    title = "Sık Kullanılanlar",
                                    apps = state.favoriteApps,
                                    fontFamily = fontFamily,
                                    onAppClick = onAppClick,
                                    onAppLongClick = onAppLongClick
                                )
                            }
                        }
                    }
                }
            }

            // Context Menu Dialog
            state.activeContextMenuApp?.let { targetApp ->
                val isPinned = state.dockApps.any { it.packageName == targetApp.packageName }
                val isFavorite = state.favoriteApps.any { it.packageName == targetApp.packageName }
                IosContextMenuDialog(
                    app = targetApp,
                    fontFamily = fontFamily,
                    isPinnedToDock = isPinned,
                    isFavorite = isFavorite,
                    onDismiss = onDismissContextMenu,
                    onLaunch = { onAppClick(targetApp) },
                    onToggleDock = { onToggleDockPin(targetApp) },
                    onToggleFavorite = { onToggleFavorite(targetApp) },
                    onAppDetails = { onOpenAppDetails(targetApp.packageName) },
                    onUninstall = { onUninstallApp(targetApp.packageName) }
                )
            }
        }
    }
}

/**
 * 2x2 Category Folder Card (Solid Matte Surface).
 */
@Composable
private fun IosCategoryFolderCard(
    title: String,
    apps: List<AppInfo>,
    fontFamily: FontFamily,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit
) {
    val folderShape = RoundedCornerShape(22.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .shadow(6.dp, folderShape)
            .clip(folderShape)
            .background(Color(0xFF1E2024))
            .border(0.5.dp, Color.White.copy(alpha = 0.12f), folderShape)
            .padding(12.dp)
    ) {
        // 2x2 Mini Icon Grid
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                if (apps.isNotEmpty()) {
                    IosAppIcon(
                        app = apps[0],
                        iconSize = 44.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[0]) },
                        onLongClick = { onAppLongClick(apps[0]) }
                    )
                } else {
                    EmptyFolderSlot()
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (apps.size > 2) {
                    IosAppIcon(
                        app = apps[2],
                        iconSize = 44.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[2]) },
                        onLongClick = { onAppLongClick(apps[2]) }
                    )
                } else {
                    EmptyFolderSlot()
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                if (apps.size > 1) {
                    IosAppIcon(
                        app = apps[1],
                        iconSize = 44.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[1]) },
                        onLongClick = { onAppLongClick(apps[1]) }
                    )
                } else {
                    EmptyFolderSlot()
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (apps.size > 3) {
                    IosAppIcon(
                        app = apps[3],
                        iconSize = 44.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[3]) },
                        onLongClick = { onAppLongClick(apps[3]) }
                    )
                } else {
                    EmptyFolderSlot()
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
private fun EmptyFolderSlot() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.05f))
    )
}

/**
 * Modern Context Menu Popup with Home Screen Pinning, Dock Pinning, and Uninstall.
 */
@Composable
private fun IosContextMenuDialog(
    app: AppInfo,
    fontFamily: FontFamily,
    isPinnedToDock: Boolean,
    isFavorite: Boolean,
    onDismiss: () -> Unit,
    onLaunch: () -> Unit,
    onToggleDock: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAppDetails: () -> Unit,
    onUninstall: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        val menuShape = RoundedCornerShape(18.dp)

        Box(
            modifier = Modifier
                .width(280.dp)
                .shadow(24.dp, menuShape, ambientColor = Color.Black.copy(alpha = 0.8f))
                .clip(menuShape)
                .background(Color(0xFF222428))
                .border(0.5.dp, Color.White.copy(alpha = 0.16f), menuShape)
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

                // Item 2: Add/Remove to Home/Favorites
                IosContextMenuItem(
                    icon = Icons.Rounded.Star,
                    title = if (isFavorite) "Ana Ekrandan Kaldır" else "Ana Ekrana Ekle",
                    fontFamily = fontFamily,
                    onClick = onToggleFavorite
                )

                // Item 3: Pin to Dock
                IosContextMenuItem(
                    icon = Icons.Rounded.PushPin,
                    title = if (isPinnedToDock) "Dock'tan Çıkar" else "Dock'a Sabitle",
                    fontFamily = fontFamily,
                    onClick = onToggleDock
                )

                // Item 4: Info
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

                // Item 5: Delete App (Destructive Red)
                IosContextMenuItem(
                    icon = Icons.Rounded.DeleteForever,
                    title = "Uygulamayı Sil",
                    fontFamily = fontFamily,
                    color = Color(0xFFFF453A),
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
            fontSize = 14.sp,
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
