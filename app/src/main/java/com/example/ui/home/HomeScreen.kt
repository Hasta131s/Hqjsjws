package com.example.ui.home

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CloudDownload
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.ui.theme.getLauncherFontFamily
import com.example.ui.wallpaper.OnlineWallpaperSheet
import com.example.viewmodel.LauncherViewModel

/**
 * Authentic Apple iPhone (iOS) Home Screen (SpringBoard).
 * Completely replaces liquid glass with an authentic iOS design language:
 * - Apple Silk & Atmospheric OLED Wallpapers
 * - iOS Big Bold Clock & Date
 * - Authentic 2x2 Weather & Battery Widgets
 * - 4-Column iOS App Grid with Squircle Icons
 * - SpringBoard "Ara" Search Pill
 * - Frosted Bottom Dock
 * - Lock Screen Flashlight & Wallpaper Quick Triggers
 * - iOS App Library (Uygulama Arşivi) with app deletion support
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

    // Upward drag opens the iOS App Library
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
        // 1. Authentic iOS 18 Wallpaper Engine (Zero water sloshing / zero caustic blobs)
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
            // Top Status / Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Online 4K Wallpapers Hub
                IconButton(
                    onClick = { viewModel.openOnlineWallpaperSheet() },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CloudDownload,
                        contentDescription = "Çevrimiçi Duvar Kağıtları",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Settings & Customizer
                IconButton(
                    onClick = { viewModel.openCustomizeSheet() },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Ayarlar",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Big Bold iOS Clock & Date Header
            if (uiState.showClockWidget) {
                IosClockHeader(
                    fontFamily = activeFontFamily,
                    onClick = { viewModel.openCustomizeSheet() }
                )
            }

            // iOS 2x2 Widgets Row
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
                            onClick = { viewModel.refreshWeather() }
                        )
                    }
                    if (uiState.showBatteryWidget) {
                        IosBatteryWidget(
                            batteryState = batteryState,
                            fontFamily = activeFontFamily,
                            onClick = { viewModel.toggleEcoMode() }
                        )
                    }
                }
            }

            // 4-Column iOS App Grid (SpringBoard)
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
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(gridApps, key = { it.packageName }) { app ->
                        IosAppIcon(
                            app = app,
                            iconSize = 58.dp,
                            fontFamily = activeFontFamily,
                            showLabel = uiState.showLabels,
                            onClick = { viewModel.launchApp(app) },
                            onLongClick = { viewModel.openContextMenu(app) }
                        )
                    }
                }
            }

            // iOS "Ara" (Search) SpringBoard Pill
            if (uiState.showSearchBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IosSearchPill(
                        fontFamily = activeFontFamily,
                        onClick = { viewModel.openDrawer() }
                    )
                }
            }

            // Bottom Bar: Flashlight + Frosted iOS Dock + Wallpaper Quick Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Real Hardware Flashlight Button (iOS Lockscreen style)
                if (uiState.showFlashlightQuickAction) {
                    IosQuickActionButton(
                        isTorch = true,
                        isTorchOn = uiState.isTorchOn,
                        onClick = { viewModel.toggleTorch(context) }
                    )
                }

                // Iconic iOS Frosted Bottom Dock
                IosDock(
                    dockApps = uiState.dockApps,
                    fontFamily = activeFontFamily,
                    onAppClick = { viewModel.launchApp(it) },
                    onAppLongClick = { viewModel.openContextMenu(it) },
                    onOpenAppLibrary = { viewModel.openDrawer() },
                    modifier = Modifier.weight(1f)
                )

                // Quick Wallpaper Action Button
                IosQuickActionButton(
                    isTorch = false,
                    isTorchOn = false,
                    onClick = { viewModel.openOnlineWallpaperSheet() }
                )
            }
        }

        // 3. Authentic iOS App Library (Uygulama Arşivi) with uninstall action
        IosAppLibrarySheet(
            state = uiState,
            fontFamily = activeFontFamily,
            onClose = { viewModel.closeDrawer() },
            onSearchChange = { viewModel.onSearchQueryChanged(it) },
            onAppClick = { viewModel.launchApp(it) },
            onAppLongClick = { viewModel.openContextMenu(it) },
            onToggleDockPin = { viewModel.toggleDockPin(it) },
            onOpenAppDetails = { viewModel.openAppDetails(it) },
            onUninstallApp = { viewModel.uninstallApp(it) },
            onDismissContextMenu = { viewModel.closeContextMenu() }
        )

        // 4. Authentic iOS Settings & Customization Sheet
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
