package com.example.ui.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.drawer.AppDrawerSheet
import com.example.ui.liquid.LiquidAppIcon
import com.example.ui.liquid.LiquidGlassCard
import com.example.ui.liquid.LiquidWallpaper
import com.example.ui.liquid.WaterRippleContainer
import com.example.ui.settings.TvnahSettingsSheet
import com.example.ui.widgets.AtmosphericWeatherWidget
import com.example.ui.widgets.HydraBatteryCapsuleWidget
import com.example.ui.widgets.LiquidClockWidget
import com.example.ui.widgets.LiquidMediaCapsuleWidget
import com.example.ui.widgets.QuickControlsWidget
import com.example.viewmodel.LauncherViewModel

@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val batteryState by viewModel.batteryState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    val scrollState = rememberScrollState()

    // Upward drag to open app drawer
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
        // Dynamic Liquid Glass Wallpaper
        LiquidWallpaper(
            wallpaperType = uiState.selectedWallpaper,
            performanceMode = uiState.performanceMode
        )

        // Interactive Water Ripple Touch Layer
        WaterRippleContainer(
            modifier = Modifier.fillMaxSize(),
            enabled = uiState.waterRippleEnabled,
            rippleColor = uiState.iconThemePack.primaryTint
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Top Glass Search & Settings Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tvnah Brand Capsule
                    LiquidGlassCard(
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.22f,
                        onClick = { viewModel.openSettings() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.WaterDrop,
                                contentDescription = "Logo",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TVNAH",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Quick Search Bar Trigger
                    LiquidGlassCard(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.18f,
                        onClick = { viewModel.openDrawer() }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF80DEEA),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Uygulama ara...",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }

                    // Settings Button
                    LiquidGlassCard(
                        shape = RoundedCornerShape(18.dp),
                        blurRefractionAlpha = 0.22f,
                        onClick = { viewModel.openSettings() }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Tune,
                                contentDescription = "Launcher Settings",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Scrollable Content Area: Widgets & Favorites
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 18.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Widget 1: Clock with live undulating water seconds wave
                    LiquidClockWidget(
                        performanceMode = uiState.performanceMode,
                        onClick = { viewModel.refreshApps() }
                    )

                    // Widget 2: Hydra Battery Capsule with real liquid sloshing
                    HydraBatteryCapsuleWidget(
                        batteryState = batteryState,
                        performanceMode = uiState.performanceMode,
                        onToggleEcoMode = { viewModel.toggleEcoMode() }
                    )

                    // Widget 3: Weather with dynamic condition & humidity droplet
                    AtmosphericWeatherWidget(
                        weatherState = weatherState,
                        onRefresh = { viewModel.refreshWeather() }
                    )

                    // Widget 4: Liquid Media Visualizer Capsule
                    LiquidMediaCapsuleWidget(
                        performanceMode = uiState.performanceMode
                    )

                    // Widget 5: Quick Controls Glass Bar
                    QuickControlsWidget(
                        onOpenSettings = { viewModel.openSettings() }
                    )

                    // Favorite Apps Carousel / Quick Access
                    if (uiState.favoriteApps.isNotEmpty()) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sık Kullanılanlar",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    color = Color(0xFF80DEEA)
                                )
                                Text(
                                    text = "Tümü için yukarı kaydır",
                                    fontSize = 11.sp,
                                    color = Color(0xFF90A4AE),
                                    modifier = Modifier.clickable { viewModel.openDrawer() }
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            LiquidGlassCard(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                blurRefractionAlpha = 0.16f
                            ) {
                                LazyRow(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(uiState.favoriteApps, key = { it.packageName }) { app ->
                                        LiquidAppIcon(
                                            app = app,
                                            iconShape = uiState.iconShape,
                                            iconThemePack = uiState.iconThemePack,
                                            iconSize = uiState.iconSize.dp,
                                            showLabel = uiState.showLabels,
                                            onClick = { viewModel.launchApp(app) },
                                            onLongClick = { viewModel.openContextMenu(app) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Floating Glass Bottom Dock
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        blurRefractionAlpha = 0.26f,
                        glowAccentColor = Color(0xFF00F0FF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Pinned Dock Apps (up to 4)
                            uiState.dockApps.forEach { app ->
                                LiquidAppIcon(
                                    app = app,
                                    iconShape = uiState.iconShape,
                                    iconThemePack = uiState.iconThemePack,
                                    iconSize = 48.dp,
                                    showLabel = false,
                                    onClick = { viewModel.launchApp(app) },
                                    onLongClick = { viewModel.openContextMenu(app) }
                                )
                            }

                            // Drawer Trigger Button in Dock
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF00F0FF).copy(alpha = 0.28f))
                                    .clickable { viewModel.openDrawer() }
                                    .testTag("open_drawer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Apps,
                                    contentDescription = "All Apps",
                                    tint = Color(0xFF00F0FF),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // App Drawer Sheet
        AppDrawerSheet(
            state = uiState,
            onClose = { viewModel.closeDrawer() },
            onSearchChange = { viewModel.onSearchQueryChanged(it) },
            onCategorySelect = { viewModel.onCategorySelected(it) },
            onAppClick = { viewModel.launchApp(it) },
            onAppLongClick = { viewModel.openContextMenu(it) },
            onToggleDockPin = { viewModel.toggleDockPin(it) },
            onOpenAppDetails = { viewModel.openAppDetails(it) },
            onDismissContextMenu = { viewModel.closeContextMenu() }
        )

        // Settings Sheet
        TvnahSettingsSheet(
            state = uiState,
            onClose = { viewModel.closeSettings() },
            onSetIconShape = { viewModel.setIconShape(it) },
            onSetIconThemePack = { viewModel.setIconThemePack(it) },
            onSetIconSize = { viewModel.setIconSize(it) },
            onToggleShowLabels = { viewModel.setShowLabels(it) },
            onSetWallpaper = { viewModel.setWallpaper(it) },
            onSetPerformanceMode = { viewModel.setPerformanceMode(it) },
            onToggleWaterRipple = { viewModel.toggleWaterRipple() },
            onSetDefaultLauncher = { viewModel.openDefaultLauncherSettings() }
        )
    }
}
