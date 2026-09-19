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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Launch
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.model.FolderConfig
import com.example.ui.theme.rememberColorOsPalette
import com.example.viewmodel.LauncherUiState
import kotlinx.coroutines.launch

/**
 * Clean Modern App Library (Uygulama Arşivi) modal sheet with ColorOS styling.
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
    onOpenFolder: (AppCategory) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val palette = rememberColorOsPalette(themeMode = state.themeMode, surfaceOpacity = state.surfaceOpacity)

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
                    if (palette.isDark) Color(0xFF090B0F).copy(alpha = 0.88f)
                    else Color(0xFFF8FAFC).copy(alpha = 0.90f)
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
                    // Modern Search Field Container
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(palette.searchPillBackground)
                            .border(0.5.dp, palette.surfaceBorderColor, RoundedCornerShape(14.dp))
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
                                tint = palette.secondaryTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (state.searchQuery.isEmpty()) {
                                    Text(
                                        text = "Uygulama Arşivi",
                                        color = palette.secondaryTextColor,
                                        fontSize = 15.sp,
                                        fontFamily = fontFamily
                                    )
                                }
                                BasicTextField(
                                    value = state.searchQuery,
                                    onValueChange = onSearchChange,
                                    textStyle = TextStyle(
                                        color = palette.primaryTextColor,
                                        fontSize = 15.sp,
                                        fontFamily = fontFamily
                                    ),
                                    cursorBrush = SolidColor(palette.accentColor),
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
                                        tint = palette.secondaryTextColor,
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
                        color = palette.accentColor,
                        fontSize = 15.sp,
                        fontFamily = fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onClose)
                            .padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // View Mode Switcher: "Kategoriler" vs "A-Z Tüm Uygulamalar" (When not searching)
                var isAlphabeticalMode by remember { mutableStateOf(false) }
                val showAlphabetical = state.searchQuery.isNotEmpty() || isAlphabeticalMode

                if (state.searchQuery.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAlphabeticalMode) "Tüm Uygulamalar (A-Z)" else "Kategoriler",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF222226))
                                    .padding(horizontal = 4.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { isAlphabeticalMode = false },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.GridView,
                                        contentDescription = "Kategori Görünümü",
                                        tint = if (!isAlphabeticalMode) Color(0xFF007AFF) else Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { isAlphabeticalMode = true },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Apps,
                                        contentDescription = "A-Z Liste Görünümü",
                                        tint = if (isAlphabeticalMode) Color(0xFF007AFF) else Color.White.copy(alpha = 0.4f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Alphabet index list & list state
                val alphabetListState = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()

                // Content: Either Search/A-Z list with Right Alphabet Bar, or 2x2 Category Folders
                if (showAlphabetical) {
                    val displayApps = if (state.searchQuery.isNotEmpty()) {
                        state.filteredApps
                    } else {
                        state.allApps.sortedBy { it.label.lowercase() }
                    }

                    // Pre-calculate index positions for each letter
                    val alphabetChars = listOf(
                        "A", "B", "C", "Ç", "D", "E", "F", "G", "H", "I", "İ", "J",
                        "K", "L", "M", "N", "O", "Ö", "P", "R", "S", "Ş", "T", "U",
                        "Ü", "V", "Y", "Z", "#"
                    )

                    Box(modifier = Modifier.fillMaxSize()) {
                        // Scrollable App List
                        LazyColumn(
                            state = alphabetListState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(end = 26.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(displayApps, key = { it.packageName }) { app ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable { onAppClick(app) }
                                        .padding(horizontal = 8.dp, vertical = 7.dp),
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
                                            fontSize = 15.sp,
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

                        // Right-Hand Vertical Alphabet Index Bar (iOS-like A-Z quick jump)
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.45f))
                                .padding(horizontal = 4.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            alphabetChars.forEach { char ->
                                val targetIndex = displayApps.indexOfFirst { app ->
                                    val firstChar = app.label.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                                    if (char == "#") {
                                        firstChar.isNotEmpty() && !firstChar[0].isLetter()
                                    } else {
                                        firstChar == char
                                    }
                                }

                                val isAvailable = targetIndex != -1

                                Text(
                                    text = char,
                                    fontSize = 10.sp,
                                    fontFamily = fontFamily,
                                    fontWeight = if (isAvailable) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isAvailable) Color(0xFF007AFF) else Color.White.copy(alpha = 0.25f),
                                    modifier = Modifier
                                        .clickable(enabled = isAvailable) {
                                            if (targetIndex != -1) {
                                                coroutineScope.launch {
                                                    alphabetListState.animateScrollToItem(targetIndex)
                                                }
                                            }
                                        }
                                        .padding(vertical = 1.dp, horizontal = 2.dp)
                                )
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
                                palette = palette,
                                onOpenFolder = { onOpenFolder(pair.first) },
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
                                    palette = palette,
                                    onOpenFolder = { onOpenFolder(AppCategory.ESSENTIALS) },
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
 * Clean Minimalist 2x2 Category Folder Card with ColorOS Frosted Glass theme support.
 */
@Composable
private fun IosCategoryFolderCard(
    title: String,
    apps: List<AppInfo>,
    fontFamily: FontFamily,
    palette: com.example.ui.theme.ColorOsPalette,
    onOpenFolder: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit
) {
    val folderShape = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(folderShape)
            .background(palette.cardBackground)
            .border(0.5.dp, palette.surfaceBorderColor, folderShape)
            .clickable(onClick = onOpenFolder)
            .padding(12.dp)
    ) {
        // 2x2 Mini Icon Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (apps.isNotEmpty()) {
                    IosAppIcon(
                        app = apps[0],
                        iconSize = 42.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[0]) },
                        onLongClick = { onAppLongClick(apps[0]) }
                    )
                } else {
                    EmptyFolderSlot(palette)
                }

                if (apps.size > 2) {
                    IosAppIcon(
                        app = apps[2],
                        iconSize = 42.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[2]) },
                        onLongClick = { onAppLongClick(apps[2]) }
                    )
                } else {
                    EmptyFolderSlot(palette)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (apps.size > 1) {
                    IosAppIcon(
                        app = apps[1],
                        iconSize = 42.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[1]) },
                        onLongClick = { onAppLongClick(apps[1]) }
                    )
                } else {
                    EmptyFolderSlot(palette)
                }

                if (apps.size > 4) {
                    // 4th slot shows +N badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(palette.searchPillBackground)
                            .border(0.5.dp, palette.surfaceBorderColor, RoundedCornerShape(10.dp))
                    ) {
                        Text(
                            text = "+${apps.size - 3}",
                            fontSize = 12.sp,
                            fontFamily = fontFamily,
                            fontWeight = FontWeight.Bold,
                            color = palette.accentColor
                        )
                    }
                } else if (apps.size == 4) {
                    IosAppIcon(
                        app = apps[3],
                        iconSize = 42.dp,
                        fontFamily = fontFamily,
                        showLabel = false,
                        onClick = { onAppClick(apps[3]) },
                        onLongClick = { onAppLongClick(apps[3]) }
                    )
                } else {
                    EmptyFolderSlot(palette)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title and Count Footer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 12.5.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = palette.primaryTextColor,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Text(
                text = "${apps.size}",
                fontSize = 11.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Medium,
                color = palette.secondaryTextColor
            )
        }
    }
}

@Composable
private fun EmptyFolderSlot(palette: com.example.ui.theme.ColorOsPalette) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(palette.searchPillBackground.copy(alpha = 0.25f))
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
