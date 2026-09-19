package com.example.ui.home

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ui.ios.IosAppIcon
import com.example.ui.ios.IosAppLibrarySheet
import com.example.ui.ios.IosBatteryWidget
import com.example.ui.ios.IosClockHeader
import com.example.ui.ios.IosDock
import com.example.ui.ios.IosQuickActionButton
import com.example.ui.ios.IosSearchPill
import com.example.ui.ios.IosSettingsSheet
import com.example.ui.ios.IosWallpaper
import com.example.ui.ios.IosWeatherWidget
import com.example.ui.theme.getClockFontFamily
import com.example.ui.theme.getClockFontWeight
import com.example.ui.theme.getLauncherFontFamily
import com.example.ui.wallpaper.OnlineWallpaperSheet
import com.example.viewmodel.LauncherViewModel

/**
 * Modern Minimalist Home Screen with custom widgets, 20 clock fonts, and clean 2-app dock.
 */
@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val batteryState by viewModel.batteryState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()
    val context = LocalContext.current

    val activeFontFamily = getLauncherFontFamily(uiState.selectedFont)
    val clockFontFamily = getClockFontFamily(uiState.clockFontOption)
    val clockFontWeight = getClockFontWeight(uiState.clockFontOption)

    // Upward drag opens the App Library
    val swipeDraggableState = rememberDraggableState { delta ->
        if (delta < -25f && !uiState.isDrawerOpen) {
            viewModel.openDrawer()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .draggable(
                state = swipeDraggableState,
                orientation = Orientation.Vertical
            )
    ) {
        // 1. Wallpaper Engine (Atmospheric & Silk gradients or Gallery photo)
        IosWallpaper(
            preset = uiState.iosWallpaperPreset,
            customImageUri = uiState.customGalleryWallpaperUri,
            isMusicReactive = uiState.isMusicReactive
        )

        // 2. Main SpringBoard Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar: Clean, distraction-free. Top-left wallpaper icon removed as requested!
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Subtle Settings icon on top-right
                IconButton(
                    onClick = { viewModel.openCustomizeSheet() },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Ayarlar & Özelleştirme",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Customizable Big Clock & Date Header (Supports 20 fonts & custom slider size)
            if (uiState.showClockWidget) {
                IosClockHeader(
                    fontFamily = clockFontFamily,
                    fontWeight = clockFontWeight,
                    fontSizeSp = uiState.clockSizeSp,
                    onClick = { viewModel.openCustomizeSheet() }
                )
            }

            // Scalable 2x2 Widgets Row
            if (uiState.showWeatherWidget || uiState.showBatteryWidget) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.showWeatherWidget) {
                        IosWeatherWidget(
                            weatherState = weatherState,
                            fontFamily = activeFontFamily,
                            scale = uiState.widgetScale,
                            onClick = { viewModel.refreshWeather() }
                        )
                    }
                    if (uiState.showBatteryWidget) {
                        IosBatteryWidget(
                            batteryState = batteryState,
                            fontFamily = activeFontFamily,
                            scale = uiState.widgetScale,
                            onClick = { viewModel.toggleEcoMode() }
                        )
                    }
                }
            }

            // App Grid with customizable columns (3, 4, or 5)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val gridApps = if (uiState.showFavoritesShelf && uiState.favoriteApps.isNotEmpty()) {
                    uiState.favoriteApps
                } else {
                    uiState.allApps
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(uiState.gridColumns),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(gridApps, key = { it.packageName }) { app ->
                        IosAppIcon(
                            app = app,
                            iconSize = 56.dp,
                            fontFamily = activeFontFamily,
                            showLabel = uiState.showLabels,
                            onClick = { viewModel.launchApp(app) },
                            onLongClick = { viewModel.openContextMenu(app) }
                        )
                    }
                }
            }

            // "Ara" (Search) Pill
            if (uiState.showSearchBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IosSearchPill(
                        fontFamily = activeFontFamily,
                        onClick = { viewModel.openDrawer() }
                    )
                }
            }

            // Bottom Area: Clean Minimal 2-app Dock + Optional Flashlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Clean Centered Dock (Defaults to 2 apps, avoiding clutter)
                IosDock(
                    dockApps = uiState.dockApps,
                    dockLimit = uiState.dockAppLimit,
                    fontFamily = activeFontFamily,
                    onAppClick = { viewModel.launchApp(it) },
                    onAppLongClick = { viewModel.openContextMenu(it) },
                    onOpenAppLibrary = { viewModel.openDrawer() }
                )

                // Optional Flashlight pinned on bottom-left if enabled
                if (uiState.showFlashlightQuickAction) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 6.dp)
                    ) {
                        IosQuickActionButton(
                            isTorch = true,
                            isTorchOn = uiState.isTorchOn,
                            onClick = { viewModel.toggleTorch(context) }
                        )
                    }
                }
            }
        }

        // 3. App Library Sheet with Favorite & Dock toggling + uninstall
        IosAppLibrarySheet(
            state = uiState,
            fontFamily = activeFontFamily,
            onClose = { viewModel.closeDrawer() },
            onSearchChange = { viewModel.onSearchQueryChanged(it) },
            onAppClick = { viewModel.launchApp(it) },
            onAppLongClick = { viewModel.openContextMenu(it) },
            onToggleDockPin = { viewModel.toggleDockPin(it) },
            onToggleFavorite = { viewModel.toggleFavoriteApp(it) },
            onOpenAppDetails = { viewModel.openAppDetails(it) },
            onUninstallApp = { viewModel.uninstallApp(it) },
            onDismissContextMenu = { viewModel.closeContextMenu() }
        )

        // 4. Settings & Customization Sheet (20 fonts, clock/widget sliders, dock limits)
        IosSettingsSheet(
            state = uiState,
            fontFamily = activeFontFamily,
            context = context,
            onClose = { viewModel.closeCustomizeSheet() },
            onSelectIosWallpaper = { viewModel.setIosWallpaperPreset(it) },
            onOpenOnlineWallpapers = { viewModel.openOnlineWallpaperSheet() },
            onPickGalleryWallpaper = { uri ->
                viewModel.applyGalleryWallpaper(context, uri, com.example.engine.SystemWallpaperTarget.BOTH)
            },
            onSelectFont = { viewModel.setFont(it) },
            onSelectClockFont = { viewModel.setClockFontOption(it) },
            onChangeClockSize = { viewModel.setClockSize(it) },
            onChangeWidgetScale = { viewModel.setWidgetScale(it) },
            onChangeDockLimit = { viewModel.setDockAppLimit(it) },
            onChangeGridColumns = { viewModel.setGridColumns(it) },
            onToggleClock = { viewModel.toggleClockWidget() },
            onToggleWeather = { viewModel.toggleWeatherWidget() },
            onToggleBattery = { viewModel.toggleBatteryWidget() },
            onToggleFavorites = { viewModel.toggleFavoritesShelf() },
            onToggleSearchBar = { viewModel.toggleSearchBar() },
            onToggleFlashlight = { viewModel.toggleFlashlightQuickAction() },
            onToggleMusicReactive = { viewModel.toggleMusicReactive() },
            onSetDefaultLauncher = { viewModel.setAsDefaultLauncher(context) }
        )

        // 5. Online Wallpapers Hub Sheet
        OnlineWallpaperSheet(
            isOpen = uiState.isOnlineWallpaperSheetOpen,
            wallpapers = uiState.onlineWallpapers,
            isLoading = uiState.isLoadingWallpapers,
            selectedCategory = uiState.selectedWallpaperCategory,
            previewWallpaper = uiState.previewWallpaper,
            onClose = { viewModel.closeOnlineWallpaperSheet() },
            onSelectCategory = { viewModel.loadOnlineWallpapers(it) },
            onSelectPreview = { viewModel.setPreviewWallpaper(it) },
            onApplyOnlineWallpaper = { wp, target ->
                viewModel.applyOnlineWallpaper(context, wp, target)
            },
            onPickGalleryImage = { uri, target ->
                viewModel.applyGalleryWallpaper(context, uri, target)
            }
        )
    }
}
