package com.example.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppInfo
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
import com.example.ui.ios.WidgetEditDialog
import com.example.ui.theme.getClockFontFamily
import com.example.ui.theme.getClockFontWeight
import com.example.ui.theme.getLauncherFontFamily
import com.example.ui.wallpaper.OnlineWallpaperSheet
import com.example.viewmodel.LauncherViewModel

/**
 * Modern Minimalist Home Screen with custom widgets, 20 clock fonts, app reordering,
 * interactive widget editing, swipe-up App Library, and fluid animations.
 */
@OptIn(ExperimentalFoundationApi::class)
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

    // Jiggle animation when in icon edit mode (iOS style)
    val infiniteTransition = rememberInfiniteTransition(label = "jiggle_transition")
    val jiggleRotation by infiniteTransition.animateFloat(
        initialValue = -1.6f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(120),
            repeatMode = RepeatMode.Reverse
        ),
        label = "jiggle_anim"
    )

    // Upward drag opens the App Library (drawer)
    val swipeDraggableState = rememberDraggableState { delta ->
        if (delta < -18f && !uiState.isDrawerOpen) {
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
            // Top Bar: Clean header with Edit Mode indicator and Settings icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Mode "Bitti" button
                if (uiState.isEditMode) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF007AFF))
                            .clickable { viewModel.setEditMode(false) }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Bitti",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Bitti",
                                fontSize = 13.sp,
                                fontFamily = activeFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

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

            // Customizable Big Clock & Date Header (Supports 20 fonts, custom slider size, long click to edit)
            if (uiState.showClockWidget) {
                IosClockHeader(
                    fontFamily = clockFontFamily,
                    fontWeight = clockFontWeight,
                    fontSizeSp = uiState.clockSizeSp,
                    onClick = { viewModel.openCustomizeSheet() },
                    onLongClick = { viewModel.openWidgetEditDialog("clock") }
                )
            }

            // Scalable 2x2 Widgets Row (With custom shape and long-press edit menu)
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
                            shape = uiState.widgetShape,
                            onClick = { viewModel.refreshWeather() },
                            onLongClick = { viewModel.openWidgetEditDialog("weather") }
                        )
                    }
                    if (uiState.showBatteryWidget) {
                        IosBatteryWidget(
                            batteryState = batteryState,
                            fontFamily = activeFontFamily,
                            scale = uiState.widgetScale,
                            shape = uiState.widgetShape,
                            onClick = { viewModel.toggleEcoMode() },
                            onLongClick = { viewModel.openWidgetEditDialog("battery") }
                        )
                    }
                }
            }

            // App Grid with customizable columns (3, 4, or 5) and Drag/Position Reordering
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
                    itemsIndexed(gridApps, key = { _, app -> app.packageName }) { index, app ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .animateItemPlacement()
                                .rotate(if (uiState.isEditMode) jiggleRotation else 0f)
                        ) {
                            IosAppIcon(
                                app = app,
                                iconSize = 56.dp,
                                fontFamily = activeFontFamily,
                                showLabel = uiState.showLabels,
                                onClick = {
                                    if (uiState.isEditMode) {
                                        viewModel.setEditMode(false)
                                    } else {
                                        viewModel.launchApp(app)
                                    }
                                },
                                onLongClick = {
                                    viewModel.setEditMode(true)
                                    viewModel.openContextMenu(app)
                                }
                            )

                            // Edit Mode Reorder / Shift Arrows badge overlay
                            if (uiState.isEditMode) {
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.TopCenter)
                                        .padding(top = 2.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.75f))
                                        .border(0.5.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (index > 0) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = "Sola Taşı",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable { viewModel.reorderApps(index, index - 1) }
                                        )
                                    }
                                    if (index < gridApps.size - 1) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowForward,
                                            contentDescription = "Sağa Taşı",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable { viewModel.reorderApps(index, index + 1) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // "Ara" (Search) Pill with Swipe-up hint
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

        // 3. Interactive Widget Edit & Deletion Dialog (Clock, Weather, Battery)
        uiState.selectedWidgetForEdit?.let { widgetKey ->
            val title = when (widgetKey) {
                "clock" -> "Saat Bileşeni"
                "weather" -> "Hava Durumu Bileşeni"
                "battery" -> "Pil Durumu Bileşeni"
                else -> "Bileşen"
            }
            WidgetEditDialog(
                widgetTitle = title,
                currentScale = if (widgetKey == "clock") (uiState.clockSizeSp / 68f) else uiState.widgetScale,
                currentShape = uiState.widgetShape,
                fontFamily = activeFontFamily,
                onScaleChange = { newScale ->
                    if (widgetKey == "clock") {
                        viewModel.setClockSize(68f * newScale)
                    } else {
                        viewModel.setWidgetScale(newScale)
                    }
                },
                onShapeChange = { newShape ->
                    viewModel.setWidgetShape(newShape)
                },
                onDeleteWidget = {
                    when (widgetKey) {
                        "clock" -> viewModel.toggleClockWidget()
                        "weather" -> viewModel.toggleWeatherWidget()
                        "battery" -> viewModel.toggleBatteryWidget()
                    }
                },
                onDismiss = { viewModel.closeWidgetEditDialog() }
            )
        }

        // 4. App Library Sheet with Favorite & Dock toggling + uninstall
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

        // 5. Settings & Customization Sheet (20 fonts, clock/widget sliders, dock limits, shapes)
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
            onChangeWidgetShape = { viewModel.setWidgetShape(it) },
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

        // 6. Online Wallpapers Hub Sheet
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

