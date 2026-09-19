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
import com.example.ui.ios.FolderConfigSheet
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

            // Customizable Dynamic Widgets Row (+6 Widgets with individual sizes & shapes + Drag & Drop Reordering)
            val visibleWidgets = uiState.homeWidgets.filter { it.isVisible }
            if (visibleWidgets.isNotEmpty()) {
                var draggedWidgetIndex by remember { mutableIntStateOf(-1) }
                var widgetDragOffsetX by remember { mutableFloatStateOf(0f) }

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
                                .rotate(if (uiState.isEditMode && !isWidgetBeingDragged) jiggleRotation else 0f)
                                .offset {
                                    if (isWidgetBeingDragged) IntOffset(widgetDragOffsetX.roundToInt(), 0)
                                    else IntOffset.Zero
                                }
                                .zIndex(if (isWidgetBeingDragged) 10f else 1f)
                                .scale(if (isWidgetBeingDragged) 1.08f else 1.0f)
                                .pointerInput(uiState.isEditMode) {
                                    if (uiState.isEditMode) {
                                        detectDragGesturesAfterLongPress(
                                            onDragStart = {
                                                draggedWidgetIndex = wIndex
                                                widgetDragOffsetX = 0f
                                            },
                                            onDrag = { change, dragAmount ->
                                                change.consume()
                                                widgetDragOffsetX += dragAmount.x
                                                val thresholdPx = 90.dp.toPx()

                                                if (widgetDragOffsetX > thresholdPx && draggedWidgetIndex < visibleWidgets.size - 1) {
                                                    val target = draggedWidgetIndex + 1
                                                    // Map visible indices back to homeWidgets indices in ViewModel
                                                    val fromOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[draggedWidgetIndex].type }
                                                    val toOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[target].type }
                                                    if (fromOriginal != -1 && toOriginal != -1) {
                                                        viewModel.reorderWidgets(fromOriginal, toOriginal)
                                                    }
                                                    draggedWidgetIndex = target
                                                    widgetDragOffsetX = 0f
                                                } else if (widgetDragOffsetX < -thresholdPx && draggedWidgetIndex > 0) {
                                                    val target = draggedWidgetIndex - 1
                                                    val fromOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[draggedWidgetIndex].type }
                                                    val toOriginal = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[target].type }
                                                    if (fromOriginal != -1 && toOriginal != -1) {
                                                        viewModel.reorderWidgets(fromOriginal, toOriginal)
                                                    }
                                                    draggedWidgetIndex = target
                                                    widgetDragOffsetX = 0f
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
                                }
                        ) {
                            IndividualWidgetWrapper(
                                config = widgetConfig,
                                fontFamily = activeFontFamily,
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
                                    viewModel.setEditMode(true)
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

                            // Edit Mode Badges: Remove / Configure & Quick move
                            if (uiState.isEditMode) {
                                // Red minus badge on top-left to delete/remove widget
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF3B30))
                                        .border(1.5.dp, Color.White, CircleShape)
                                        .clickable {
                                            viewModel.removeHomeWidget(widgetConfig.type)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Widgetı Kaldır",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Quick reorder directional arrows (Left & Right)
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = 6.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.85f))
                                        .border(0.5.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (wIndex > 0) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowBack,
                                            contentDescription = "Sola Taşı",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    val fromOrig = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[wIndex].type }
                                                    val toOrig = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[wIndex - 1].type }
                                                    if (fromOrig != -1 && toOrig != -1) {
                                                        viewModel.reorderWidgets(fromOrig, toOrig)
                                                    }
                                                }
                                        )
                                    }
                                    if (wIndex > 0 && wIndex < visibleWidgets.size - 1) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    if (wIndex < visibleWidgets.size - 1) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowForward,
                                            contentDescription = "Sağa Taşı",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    val fromOrig = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[wIndex].type }
                                                    val toOrig = uiState.homeWidgets.indexOfFirst { it.type == visibleWidgets[wIndex + 1].type }
                                                    if (fromOrig != -1 && toOrig != -1) {
                                                        viewModel.reorderWidgets(fromOrig, toOrig)
                                                    }
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // App Grid with customizable columns (3, 4, or 5), drag & drop reordering, and delete/remove
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

                LazyVerticalGrid(
                    columns = GridCells.Fixed(uiState.gridColumns),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(gridApps, key = { _, app -> app.packageName }) { index, app ->
                        val isBeingDragged = draggedAppIndex == index

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .animateItemPlacement()
                                .rotate(if (uiState.isEditMode && !isBeingDragged) jiggleRotation else 0f)
                                .offset {
                                    if (isBeingDragged) IntOffset(dragOffsetX.roundToInt(), dragOffsetY.roundToInt())
                                    else IntOffset.Zero
                                }
                                .zIndex(if (isBeingDragged) 10f else 1f)
                                .scale(if (isBeingDragged) 1.12f else 1.0f)
                                .pointerInput(uiState.isEditMode) {
                                    if (uiState.isEditMode) {
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

                                                // Dynamic swap when dragged horizontally or vertically
                                                val colWidth = 90.dp.toPx()
                                                val rowHeight = 90.dp.toPx()
                                                val cols = uiState.gridColumns

                                                if (dragOffsetX > colWidth && (draggedAppIndex % cols) < cols - 1) {
                                                    val target = (draggedAppIndex + 1).coerceAtMost(gridApps.size - 1)
                                                    viewModel.reorderApps(draggedAppIndex, target)
                                                    draggedAppIndex = target
                                                    dragOffsetX = 0f
                                                } else if (dragOffsetX < -colWidth && (draggedAppIndex % cols) > 0) {
                                                    val target = (draggedAppIndex - 1).coerceAtLeast(0)
                                                    viewModel.reorderApps(draggedAppIndex, target)
                                                    draggedAppIndex = target
                                                    dragOffsetX = 0f
                                                } else if (dragOffsetY > rowHeight && draggedAppIndex + cols < gridApps.size) {
                                                    val target = draggedAppIndex + cols
                                                    viewModel.reorderApps(draggedAppIndex, target)
                                                    draggedAppIndex = target
                                                    dragOffsetY = 0f
                                                } else if (dragOffsetY < -rowHeight && draggedAppIndex - cols >= 0) {
                                                    val target = draggedAppIndex - cols
                                                    viewModel.reorderApps(draggedAppIndex, target)
                                                    draggedAppIndex = target
                                                    dragOffsetY = 0f
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
                                    viewModel.setEditMode(true)
                                    viewModel.openContextMenu(app)
                                }
                            )

                            // Edit Mode: Red minus (-) circle badge to Remove from Home or Uninstall Completely
                            if (uiState.isEditMode) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .offset(x = 6.dp, y = (-2).dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFF3B30))
                                        .border(1.5.dp, Color.White, CircleShape)
                                        .clickable {
                                            viewModel.openAppRemoveOrDeleteDialog(app)
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Remove,
                                        contentDescription = "Uygulamayı Kaldır veya Sil",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Quick reorder directional arrows
                                Row(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = 4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black.copy(alpha = 0.80f))
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
                                                .size(15.dp)
                                                .clickable { viewModel.reorderApps(index, index - 1) }
                                        )
                                    }
                                    if (index < gridApps.size - 1) {
                                        Icon(
                                            imageVector = Icons.Rounded.ArrowForward,
                                            contentDescription = "Sağa Taşı",
                                            tint = Color.White,
                                            modifier = Modifier
                                                .size(15.dp)
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

        // 7. App Library Sheet with Favorite & Dock toggling + uninstall + folder customization
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
            onOpenFolder = { viewModel.openFolder(it) },
            onOpenFolderConfig = { viewModel.openFolderConfigSheet() }
        )

        // 7.1 Expanded Category Folder Dialog (with custom shapes, opacity, and grid columns)
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
                folderConfig = uiState.folderConfig,
                fontFamily = activeFontFamily,
                onDismiss = { viewModel.closeFolder() },
                onAppClick = { viewModel.launchApp(it) },
                onAppLongClick = { viewModel.openContextMenu(it) },
                onOpenFolderSettings = {
                    viewModel.openFolderConfigSheet()
                }
            )
        }

        // 7.2 Folder Customization Sheet (Shape, Opacity & Grid layout)
        FolderConfigSheet(
            isOpen = uiState.isFolderConfigSheetOpen,
            folderConfig = uiState.folderConfig,
            fontFamily = activeFontFamily,
            onClose = { viewModel.closeFolderConfigSheet() },
            onShapeChange = { viewModel.setFolderShape(it) },
            onOpacityChange = { viewModel.setFolderOpacity(it) },
            onGridColumnsChange = { viewModel.setFolderGridColumns(it) }
        )

        // 8. Settings & Customization Sheet
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
            onChangeFolderShape = { viewModel.setFolderShape(it) },
            onChangeFolderOpacity = { viewModel.setFolderOpacity(it) },
            onChangeFolderGridColumns = { viewModel.setFolderGridColumns(it) },
            onOpenFolderConfig = { viewModel.openFolderConfigSheet() },
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
