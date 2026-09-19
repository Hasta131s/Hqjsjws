package com.example.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import com.example.model.AppInfo
import com.example.model.IndividualWidgetConfig
import com.example.model.WidgetSize
import com.example.model.WidgetType
import com.example.ui.ios.AddWidgetCatalogSheet
import com.example.ui.ios.AppRemoveOrDeleteDialog
import com.example.ui.ios.BatteryWidgetContent
import com.example.ui.ios.CalendarWidgetContent
import com.example.ui.ios.ClockWidgetContent
import com.example.ui.ios.ControlsWidgetContent
import com.example.ui.ios.ExpandedFolderDialog
import com.example.ui.ios.HomeActionMenuSheet
import com.example.ui.ios.IndividualWidgetEditDialog
import com.example.ui.ios.IndividualWidgetWrapper
import com.example.ui.ios.IosAppIcon
import com.example.ui.ios.IosAppLibrarySheet
import com.example.ui.ios.IosClockHeader
import com.example.ui.ios.IosDock
import com.example.ui.ios.IosQuickActionButton
import com.example.ui.ios.IosSearchPill
import com.example.ui.ios.IosSettingsSheet
import com.example.ui.ios.IosWallpaper
import com.example.ui.ios.MediaWidgetContent
import com.example.ui.ios.NotesWidgetContent
import com.example.ui.ios.SpeedDialWidgetContent
import com.example.ui.ios.WeatherWidgetContent
import com.example.ui.theme.getClockFontFamily
import com.example.ui.theme.getClockFontWeight
import com.example.ui.theme.getLauncherFontFamily
import com.example.ui.wallpaper.OnlineWallpaperSheet
import com.example.viewmodel.LauncherViewModel
import kotlin.math.roundToInt

/**
 * Modern Minimalist Home Screen with:
 * - Long press empty space menu (Add widgets, change wallpaper, edit icons)
 * - +6 distinct custom widgets with individual size (Kare, Yatay Dikdörtgen, Dikey Dikdörtgen) and shape adjustment
 * - Drag and drop & arrow reordering for apps
 * - Delete from home screen vs uninstall app completely confirmation
 * - Swipe-up to open App Drawer
 * - System launcher integration
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

    // Upward drag opens the App Library (drawer) - responsive swipe up anywhere on home screen
    val swipeDraggableState = rememberDraggableState { delta ->
        if (delta < -10f && !uiState.isDrawerOpen && !uiState.isEditMode) {
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
            .combinedClickable(
                onClick = {
                    if (uiState.isEditMode) viewModel.setEditMode(false)
                },
                onLongClick = {
                    // Boş alana basılı tutunca duvar kağıdı seçme ve widget ekleme menüsü açılır
                    viewModel.openHomeMenu()
                }
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
            // Top Bar: Clean header with Edit Mode indicator, Add Widget button, and Settings icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left action: Add Widget button or Done button
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
                    // Plus button to open Widget Catalog / Home Menu directly
                    IconButton(
                        onClick = { viewModel.openHomeMenu() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Widget & Duvar Kağıdı Ekle",
                            tint = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // Center: subtle hint if in edit mode
                if (uiState.isEditMode) {
                    Text(
                        text = "Düzenleme Modu",
                        fontSize = 12.sp,
                        fontFamily = activeFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Right: Settings icon
                IconButton(
                    onClick = { viewModel.openCustomizeSheet() },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Ayarlar & Özelleştirme",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // Big Clock Header (Supports 20 fonts, custom slider size, long click to edit)
            if (uiState.showClockWidget) {
                IosClockHeader(
                    fontFamily = clockFontFamily,
                    fontWeight = clockFontWeight,
                    fontSizeSp = uiState.clockSizeSp,
                    onClick = { viewModel.openCustomizeSheet() },
                    onLongClick = { viewModel.openHomeMenu() }
                )
            }

            // Customizable Dynamic Widgets Row (+6 Widgets with individual sizes & shapes + Universal Drag & Drop Reordering)
            val visibleWidgets = uiState.homeWidgets.filter { it.isVisible }
            if (visibleWidgets.isNotEmpty()) {
                var draggedWidgetIndex by remember { mutableIntStateOf(-1) }
                var widgetDragOffsetX by remember { mutableFloatStateOf(0f) }
                val widgetBoundsMap = remember { mutableMapOf<Int, androidx.compose.ui.geometry.Rect>() }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(visibleWidgets, key = { _, it -> it.type.name }) { wIndex, widgetConfig ->
                        val isWidgetBeingDragged = draggedWidgetIndex == wIndex

                        Box(
                            modifier = Modifier
                                .animateItemPlacement()
                                .onGloballyPositioned { layoutCoordinates ->
                                    if (!isWidgetBeingDragged) {
                                        widgetBoundsMap[wIndex] = layoutCoordinates.boundsInWindow()
                                    }
                                }
                                .rotate(if (uiState.isEditMode && !isWidgetBeingDragged) jiggleRotation else 0f)
                                .offset {
                                    if (isWidgetBeingDragged) IntOffset(widgetDragOffsetX.roundToInt(), 0)
                                    else IntOffset.Zero
                                }
                                .zIndex(if (isWidgetBeingDragged) 100f else 1f)
                                .scale(if (isWidgetBeingDragged) 1.08f else 1.0f)
                                .pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            draggedWidgetIndex = wIndex
                                            widgetDragOffsetX = 0f
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            widgetDragOffsetX += dragAmount.x

                                            // Real-time target collision calculation
                                            val currentBounds = widgetBoundsMap[draggedWidgetIndex]
                                            if (currentBounds != null) {
                                                val currentCenter = Offset(
                                                    x = currentBounds.center.x + widgetDragOffsetX,
                                                    y = currentBounds.center.y
                                                )

                                                for ((targetIdx, bounds) in widgetBoundsMap) {
                                                    if (targetIdx != draggedWidgetIndex && bounds.contains(currentCenter)) {
                                                        val fromOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[draggedWidgetIndex].type }
                                                        val toOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[targetIdx].type }
                                                        if (fromOriginal != -1 && toOriginal != -1) {
                                                            viewModel.reorderWidgets(fromOriginal, toOriginal)
                                                        }
                                                        draggedWidgetIndex = targetIdx
                                                        widgetDragOffsetX = 0f
                                                        break
                                                    }
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            draggedWidgetIndex = -1
                                            widgetDragOffsetX = 0f
                                        },
                                        onDragCancel = {
                                            draggedWidgetIndex = -1
                                            widgetDragOffsetX = 0f
                                        }
                                    )
                                }
                        ) {
                            IndividualWidgetWrapper(
                                config = widgetConfig,
                                fontFamily = activeFontFamily,
                                themeMode = uiState.themeMode,
                                surfaceOpacity = uiState.surfaceOpacity,
                                blurRadiusDp = uiState.blurRadiusDp,
                                onClick = {
                                    if (uiState.isEditMode) {
                                        viewModel.setEditMode(false)
                                    } else {
                                        when (widgetConfig.type) {
                                            WidgetType.WEATHER -> viewModel.refreshWeather()
                                            WidgetType.BATTERY -> viewModel.toggleEcoMode()
                                            WidgetType.CONTROLS -> viewModel.toggleTorch(context)
                                            else -> {}
                                        }
                                    }
                                },
                                onLongClick = {
                                    viewModel.openIndividualWidgetEditDialog(widgetConfig)
                                }
                            ) {
                                when (widgetConfig.type) {
                                    WidgetType.CLOCK -> ClockWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily
                                    )
                                    WidgetType.WEATHER -> WeatherWidgetContent(
                                        weatherState = weatherState,
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily
                                    )
                                    WidgetType.BATTERY -> BatteryWidgetContent(
                                        batteryState = batteryState,
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily,
                                        onToggleEcoMode = { viewModel.toggleEcoMode() }
                                    )
                                    WidgetType.MEDIA -> MediaWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily
                                    )
                                    WidgetType.CONTROLS -> ControlsWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily,
                                        isTorchOn = uiState.isTorchOn,
                                        onToggleTorch = { viewModel.toggleTorch(context) }
                                    )
                                    WidgetType.CALENDAR -> CalendarWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily
                                    )
                                    WidgetType.NOTES -> NotesWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily
                                    )
                                    WidgetType.SPEED_DIAL -> SpeedDialWidgetContent(
                                        size = widgetConfig.size,
                                        fontFamily = activeFontFamily,
                                        context = context
                                    )
                                }
                            }

                            // Edit Mode Badges: Remove
                            if (uiState.isEditMode) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE53935))
                                        .border(1.5.dp, Color.White, CircleShape)
                                        .clickable {
                                            viewModel.removeHomeWidget(widgetConfig.type)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Widgetı Kaldır",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // App Grid with customizable columns (3, 4, or 5) and Universal Fluid Drag & Drop Reordering
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

                var draggedAppIndex by remember { mutableIntStateOf(-1) }
                var dragOffsetX by remember { mutableFloatStateOf(0f) }
                var dragOffsetY by remember { mutableFloatStateOf(0f) }
                val itemBoundsMap = remember { mutableMapOf<Int, androidx.compose.ui.geometry.Rect>() }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(uiState.gridColumns),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(gridApps, key = { _, app -> app.packageName }) { index, app ->
                        val isBeingDragged = draggedAppIndex == index

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .animateItemPlacement()
                                .onGloballyPositioned { layoutCoordinates ->
                                    if (!isBeingDragged) {
                                        itemBoundsMap[index] = layoutCoordinates.boundsInWindow()
                                    }
                                }
                                .rotate(if (uiState.isEditMode && !isBeingDragged) jiggleRotation else 0f)
                                .offset {
                                    if (isBeingDragged) IntOffset(dragOffsetX.roundToInt(), dragOffsetY.roundToInt())
                                    else IntOffset.Zero
                                }
                                .zIndex(if (isBeingDragged) 100f else 1f)
                                .scale(if (isBeingDragged) 1.15f else 1.0f)
                                .pointerInput(Unit) {
                                    detectDragGesturesAfterLongPress(
                                        onDragStart = {
                                            draggedAppIndex = index
                                            dragOffsetX = 0f
                                            dragOffsetY = 0f
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            dragOffsetX += dragAmount.x
                                            dragOffsetY += dragAmount.y

                                            // Real-time target hit detection based on finger position
                                            val currentBounds = itemBoundsMap[draggedAppIndex]
                                            if (currentBounds != null) {
                                                val currentCenter = Offset(
                                                    x = currentBounds.center.x + dragOffsetX,
                                                    y = currentBounds.center.y + dragOffsetY
                                                )

                                                for ((targetIdx, bounds) in itemBoundsMap) {
                                                    if (targetIdx != draggedAppIndex && bounds.contains(currentCenter)) {
                                                        viewModel.reorderApps(draggedAppIndex, targetIdx)
                                                        draggedAppIndex = targetIdx
                                                        dragOffsetX = 0f
                                                        dragOffsetY = 0f
                                                        break
                                                    }
                                                }
                                            }
                                        },
                                        onDragEnd = {
                                            draggedAppIndex = -1
                                            dragOffsetX = 0f
                                            dragOffsetY = 0f
                                        },
                                        onDragCancel = {
                                            draggedAppIndex = -1
                                            dragOffsetX = 0f
                                            dragOffsetY = 0f
                                        }
                                    )
                                }
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
                                    viewModel.openContextMenu(app)
                                }
                            )

                            // Edit Mode: Subtle clean remove badge
                            if (uiState.isEditMode) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = 6.dp, y = (-2).dp)
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE53935))
                                        .border(1.5.dp, Color.White, CircleShape)
                                        .clickable {
                                            viewModel.openAppRemoveOrDeleteDialog(app)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Uygulamayı Kaldır veya Sil",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // "Ara" (Search) Pill with Swipe-up hint
            if (uiState.showSearchBar) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Small swipe up chevron indicator
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Yukarı kaydır",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { viewModel.openDrawer() }
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    IosSearchPill(
                        fontFamily = activeFontFamily,
                        themeMode = uiState.themeMode,
                        surfaceOpacity = uiState.surfaceOpacity,
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
                // Clean Centered Dock
                IosDock(
                    dockApps = uiState.dockApps,
                    dockLimit = uiState.dockAppLimit,
                    fontFamily = activeFontFamily,
                    themeMode = uiState.themeMode,
                    surfaceOpacity = uiState.surfaceOpacity,
                    blurRadiusDp = uiState.blurRadiusDp,
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

        // 3. Long-Press Empty Space Menu Sheet (Duvar kağıdı seçme & Widget ekleme)
        if (uiState.isHomeMenuOpen) {
            HomeActionMenuSheet(
                fontFamily = activeFontFamily,
                onAddWidget = { viewModel.openAddWidgetSheet() },
                onChangeWallpaper = { viewModel.openOnlineWallpaperSheet() },
                onEditIcons = { viewModel.setEditMode(true) },
                onOpenSettings = { viewModel.openCustomizeSheet() },
                onSetDefaultLauncher = { viewModel.openDefaultLauncherSettings(context) },
                onDismiss = { viewModel.closeHomeMenu() }
            )
        }

        // 4. Add Widget Catalog Sheet (+6 Farklı Widget Ekleme)
        if (uiState.isAddWidgetSheetOpen) {
            AddWidgetCatalogSheet(
                fontFamily = activeFontFamily,
                onAddWidget = { type, size ->
                    viewModel.addHomeWidget(type, size)
                },
                onDismiss = { viewModel.closeAddWidgetSheet() }
            )
        }

        // 5. Individual Widget Size & Shape Edit Dialog (Dikdörtgen, dikey, yatay dikdörtgen)
        uiState.selectedIndividualWidgetForEdit?.let { widgetConfig ->
            IndividualWidgetEditDialog(
                config = widgetConfig,
                fontFamily = activeFontFamily,
                onSizeChange = { newSize ->
                    viewModel.updateWidgetSize(widgetConfig.type, newSize)
                },
                onShapeChange = { newShape ->
                    viewModel.updateWidgetShape(widgetConfig.type, newShape)
                },
                onScaleChange = { newScale ->
                    viewModel.updateWidgetScale(widgetConfig.type, newScale)
                },
                onHorizontalScaleChange = { newHorizontalScale ->
                    viewModel.updateWidgetHorizontalScale(widgetConfig.type, newHorizontalScale)
                },
                onVerticalScaleChange = { newVerticalScale ->
                    viewModel.updateWidgetVerticalScale(widgetConfig.type, newVerticalScale)
                },
                onDeleteWidget = {
                    viewModel.removeHomeWidget(widgetConfig.type)
                },
                onDismiss = { viewModel.closeIndividualWidgetEditDialog() }
            )
        }

        // 6. App Remove from Home Screen vs Uninstall Device Dialog
        uiState.appToRemoveOrDelete?.let { app ->
            AppRemoveOrDeleteDialog(
                app = app,
                fontFamily = activeFontFamily,
                onRemoveFromHome = { viewModel.removeFromHomeScreen(app) },
                onUninstallDevice = { viewModel.uninstallApp(app) },
                onDismiss = { viewModel.closeAppRemoveOrDeleteDialog() }
            )
        }

        // 7. App Library Sheet with Favorite & Dock toggling + uninstall
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
            onDismissContextMenu = { viewModel.closeContextMenu() },
            onOpenFolder = { viewModel.openFolder(it) }
        )

        // 7.1 Expanded Category Folder Dialog
        uiState.expandedFolderCategory?.let { category ->
            val appsInCat = uiState.allApps.filter { it.category == category }
            val categoryTitle = when (category) {
                com.example.model.AppCategory.ESSENTIALS -> "Önemli"
                com.example.model.AppCategory.SOCIAL -> "Sosyal"
                com.example.model.AppCategory.MEDIA -> "Medya & Eğlence"
                com.example.model.AppCategory.TOOLS -> "Araçlar"
                com.example.model.AppCategory.GAMES -> "Oyunlar"
                com.example.model.AppCategory.SYSTEM -> "Sistem"
                com.example.model.AppCategory.ALL -> "Tüm Uygulamalar"
            }
            ExpandedFolderDialog(
                category = category,
                categoryTitle = categoryTitle,
                apps = appsInCat,
                fontFamily = activeFontFamily,
                onDismiss = { viewModel.closeFolder() },
                onAppClick = { viewModel.launchApp(it) },
                onAppLongClick = { viewModel.openContextMenu(it) }
            )
        }

        // 8. Settings & Customization Sheet
        IosSettingsSheet(
            state = uiState,
            fontFamily = activeFontFamily,
            context = context,
            onClose = { viewModel.closeCustomizeSheet() },
            onSelectThemeMode = { viewModel.setThemeMode(it) },
            onChangeSurfaceOpacity = { viewModel.setSurfaceOpacity(it) },
            onChangeBlurRadius = { viewModel.setBlurRadiusDp(it) },
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
            onSetDefaultLauncher = { viewModel.openDefaultLauncherSettings(context) }
        )

        // 9. Online Wallpapers Hub Sheet
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
