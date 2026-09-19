package com.example.ui.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material.icons.rounded.PushPin
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppCategory
import com.example.model.AppInfo
import com.example.ui.liquid.LiquidAppIcon
import com.example.ui.liquid.LiquidGlassCard
import com.example.ui.liquid.LiquidGlassTheme
import com.example.viewmodel.LauncherUiState
import kotlinx.coroutines.launch

@Composable
fun AppDrawerSheet(
    state: LauncherUiState,
    onClose: () -> Unit,
    onSearchChange: (String) -> Unit,
    onCategorySelect: (AppCategory) -> Unit,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    onToggleDockPin: (AppInfo) -> Unit,
    onOpenAppDetails: (String) -> Unit,
    onDismissContextMenu: () -> Unit,
    modifier: Modifier = Modifier,
    onUninstallApp: (String) -> Unit = {}
) {
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val alphabet = ('A'..'Z').toList()

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
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xEE040D1A),
                            Color(0xF5061426),
                            Color(0xFC020811)
                        )
                    )
                )
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header drag pill & close
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.White.copy(alpha = 0.35f))
                            .clickable(onClick = onClose)
                    )
                }

                // Liquid Search Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    LiquidGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        blurRefractionAlpha = 0.22f,
                        glowAccentColor = Color(0xFF00F0FF)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            BasicTextField(
                                value = state.searchQuery,
                                onValueChange = onSearchChange,
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                cursorBrush = SolidColor(Color(0xFF00F0FF)),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("app_search_input"),
                                decorationBox = { innerTextField ->
                                    if (state.searchQuery.isEmpty()) {
                                        Text(
                                            text = "Uygulama ara...",
                                            color = Color.White.copy(alpha = 0.45f),
                                            fontSize = 15.sp
                                        )
                                    }
                                    innerTextField()
                                }
                            )

                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchChange("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "Clear search",
                                        tint = Color(0xFFB0BEC5),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = onClose,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = "Close drawer",
                                        tint = Color(0xFFB0BEC5),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Category Chips Row
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(AppCategory.values()) { category ->
                        val isSelected = state.selectedCategory == category
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.28f)
                                    else Color.White.copy(alpha = 0.08f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color(0xFF00F0FF).copy(alpha = 0.8f)
                                    else Color.White.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable { onCategorySelect(category) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = category.titleTr,
                                fontSize = 12.5.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF00F0FF) else Color(0xFFCFD8DC)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // App Grid with Alphabet Scrubber on the side
                Row(modifier = Modifier.fillMaxSize()) {
                    if (state.filteredApps.isEmpty()) {
                        // Empty search state
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Rounded.WaterDrop,
                                    contentDescription = "Empty",
                                    tint = Color(0xFF00F0FF).copy(alpha = 0.4f),
                                    modifier = Modifier.size(54.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Sonuç bulunamadı",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Farklı bir arama terimi deneyin",
                                    color = Color(0xFF90A4AE),
                                    fontSize = 12.5.sp
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            state = gridState,
                            contentPadding = PaddingValues(start = 14.dp, end = 6.dp, top = 8.dp, bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.weight(1f)
                        ) {
                            items(state.filteredApps, key = { it.packageName }) { app ->
                                LiquidAppIcon(
                                    app = app,
                                    iconShape = state.iconShape,
                                    iconThemePack = state.iconThemePack,
                                    iconSize = state.iconSize.dp,
                                    showLabel = state.showLabels,
                                    onClick = { onAppClick(app) },
                                    onLongClick = { onAppLongClick(app) }
                                )
                            }
                        }

                        // Alphabet Quick Scrubber
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight()
                                .padding(vertical = 12.dp)
                        ) {
                            alphabet.forEach { letter ->
                                Text(
                                    text = letter.toString(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF80DEEA).copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .clickable {
                                            val index = state.filteredApps.indexOfFirst {
                                                it.label.startsWith(letter, ignoreCase = true)
                                            }
                                            if (index >= 0) {
                                                scope.launch { gridState.animateScrollToItem(index) }
                                            }
                                        }
                                        .padding(vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Context Menu Dialog on Long Press
    if (state.activeContextMenuApp != null) {
        val app = state.activeContextMenuApp
        val isPinned = state.dockApps.any { it.packageName == app.packageName }

        Dialog(onDismissRequest = onDismissContextMenu) {
            LiquidGlassCard(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(16.dp),
                shape = RoundedCornerShape(26.dp),
                blurRefractionAlpha = 0.28f,
                glowAccentColor = Color(0xFF00F0FF)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LiquidAppIcon(
                        app = app,
                        iconShape = state.iconShape,
                        iconThemePack = state.iconThemePack,
                        iconSize = 64.dp,
                        showLabel = true,
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action 1: Launch
                    ContextMenuActionItem(
                        icon = Icons.Rounded.Apps,
                        title = "Uygulamayı Aç",
                        onClick = { onAppClick(app) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action 2: Pin / Unpin from Dock
                    ContextMenuActionItem(
                        icon = if (isPinned) Icons.Rounded.PushPin else Icons.Rounded.Pin,
                        title = if (isPinned) "Dock'tan Çıkar" else "Dock'a Sabitle",
                        onClick = { onToggleDockPin(app) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action 3: App Details
                    ContextMenuActionItem(
                        icon = Icons.Rounded.Info,
                        title = "Uygulama Bilgileri",
                        onClick = { onOpenAppDetails(app.packageName) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action 4: Uninstall / Delete App
                    ContextMenuActionItem(
                        icon = Icons.Rounded.DeleteForever,
                        title = "Uygulamayı Kaldır (Sil)",
                        iconTint = Color(0xFFFF5252),
                        textColor = Color(0xFFFF8A80),
                        onClick = { onUninstallApp(app.packageName) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Close
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.08f))
                            .clickable(onClick = onDismissContextMenu)
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = "Kapat",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFCFD8DC)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContextMenuActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    iconTint: Color = Color(0xFF00F0FF),
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}
