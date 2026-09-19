package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.AppLauncherManager
import com.example.engine.BatteryMonitor
import com.example.engine.DeviceFlashlightManager
import com.example.engine.MusicVisualizerManager
import com.example.engine.OnlineWallpaperRepository
import com.example.engine.SystemWallpaperManager
import com.example.engine.SystemWallpaperTarget
import com.example.model.AppCategory
import com.example.model.AppInfo
import com.example.model.BatteryState
import com.example.model.IconShape
import com.example.model.IconThemePack
import com.example.model.IosWallpaperPreset
import com.example.model.LiquidWallpaperConfig
import com.example.model.LiquidWallpaperType
import com.example.model.OnlineWallpaper
import com.example.model.PerformanceMode
import com.example.model.WallpaperCategory
import com.example.model.WeatherState
import com.example.model.IndividualWidgetConfig
import com.example.model.WidgetShape
import com.example.model.WidgetSize
import com.example.model.WidgetType
import com.example.ui.theme.ClockFontOption
import com.example.ui.theme.LauncherFont
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class LauncherUiState(
    val allApps: List<AppInfo> = emptyList(),
    val filteredApps: List<AppInfo> = emptyList(),
    val dockApps: List<AppInfo> = emptyList(),
    val favoriteApps: List<AppInfo> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: AppCategory = AppCategory.ALL,
    val iconShape: IconShape = IconShape.SQUIRCLE,
    val iconThemePack: IconThemePack = IconThemePack.AURA_CYAN,
    val iconSize: Float = 58f,
    val showLabels: Boolean = true,
    val selectedWallpaper: LiquidWallpaperType = LiquidWallpaperType.HYDRA_ABYSS,
    val wallpaperConfig: LiquidWallpaperConfig = LiquidWallpaperConfig.defaultFor(LiquidWallpaperType.HYDRA_ABYSS),
    val performanceMode: PerformanceMode = PerformanceMode.AQUA_FLOW_120,
    val waterRippleEnabled: Boolean = true,
    val isDrawerOpen: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val isWallpaperManagerOpen: Boolean = false,
    val isCustomizeSheetOpen: Boolean = false,
    val isOnlineWallpaperSheetOpen: Boolean = false,
    val activeContextMenuApp: AppInfo? = null,
    val isLoadingApps: Boolean = true,
    // iOS Minimalist & Customization Features
    val showClockWidget: Boolean = true,
    val showWeatherWidget: Boolean = true,
    val showBatteryWidget: Boolean = true,
    val showMediaWidget: Boolean = true,
    val showFlashlightQuickAction: Boolean = true,
    val showFavoritesShelf: Boolean = true,
    val showSearchBar: Boolean = true,
    val selectedFont: LauncherFont = LauncherFont.OUTFIT,
    val isMusicReactive: Boolean = false,
    val isTorchOn: Boolean = false,
    val customGalleryWallpaperUri: String? = null,
    val onlineWallpapers: List<OnlineWallpaper> = emptyList(),
    val isLoadingWallpapers: Boolean = false,
    val selectedWallpaperCategory: WallpaperCategory = WallpaperCategory.ALL,
    val previewWallpaper: OnlineWallpaper? = null,
    val iosWallpaperPreset: IosWallpaperPreset = IosWallpaperPreset.IOS_18_NEBULA,
    val clockSizeSp: Float = 68f,
    val widgetScale: Float = 1.0f,
    val widgetShape: WidgetShape = WidgetShape.ROUNDED_SQUIRCLE,
    val clockFontOption: ClockFontOption = ClockFontOption.OUTFIT_BOLD,
    val dockAppLimit: Int = 2,
    val gridColumns: Int = 4,
    val isEditMode: Boolean = false,
    val selectedWidgetForEdit: String? = null,
    val homeWidgets: List<IndividualWidgetConfig> = listOf(
        IndividualWidgetConfig(type = WidgetType.WEATHER, size = WidgetSize.SQUARE),
        IndividualWidgetConfig(type = WidgetType.BATTERY, size = WidgetSize.SQUARE),
        IndividualWidgetConfig(type = WidgetType.CONTROLS, size = WidgetSize.SQUARE),
        IndividualWidgetConfig(type = WidgetType.MEDIA, size = WidgetSize.HORIZONTAL)
    ),
    val isHomeMenuOpen: Boolean = false,
    val isAddWidgetSheetOpen: Boolean = false,
    val appToRemoveOrDelete: AppInfo? = null,
    val selectedIndividualWidgetForEdit: IndividualWidgetConfig? = null
)

class LauncherViewModel(application: Application) : AndroidViewModel(application) {

    private val launcherManager = AppLauncherManager(application)
    private val batteryMonitor = BatteryMonitor(application)

    private val _uiState = MutableStateFlow(LauncherUiState())
    val uiState: StateFlow<LauncherUiState> = _uiState.asStateFlow()

    val batteryState: StateFlow<BatteryState> = batteryMonitor.observeBatteryState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = BatteryState()
        )

    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState: StateFlow<WeatherState> = _weatherState.asStateFlow()

    init {
        refreshApps()
        observeFlashlight()
        loadOnlineWallpapers(WallpaperCategory.ALL)
    }

    fun refreshApps() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingApps = true)
            val apps = launcherManager.loadInstalledApps()
            
            // Pick default dock apps (Phone, Messages, Chrome/Browser, Camera)
            val dock = apps.filter {
                it.packageName.contains("dialer") || it.packageName.contains("phone") ||
                        it.packageName.contains("messaging") || it.packageName.contains("chrome") ||
                        it.packageName.contains("camera")
            }.take(4).ifEmpty { apps.take(4) }

            val favorites = apps.take(8)

            _uiState.value = _uiState.value.copy(
                allApps = apps,
                filteredApps = apps,
                dockApps = dock,
                favoriteApps = favorites,
                isLoadingApps = false
            )
            applyFilter()
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilter()
    }

    fun onCategorySelected(category: AppCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilter()
    }

    private fun applyFilter() {
        val state = _uiState.value
        val query = state.searchQuery.trim().lowercase()
        val category = state.selectedCategory

        val filtered = state.allApps.filter { app ->
            val matchesQuery = query.isEmpty() ||
                    app.label.lowercase().contains(query) ||
                    app.packageName.lowercase().contains(query)

            val matchesCategory = category == AppCategory.ALL || app.category == category

            matchesQuery && matchesCategory
        }

        _uiState.value = _uiState.value.copy(filteredApps = filtered)
    }

    fun launchApp(app: AppInfo): Boolean {
        closeContextMenu()
        closeDrawer()
        return launcherManager.launchApp(app)
    }

    fun openAppDetails(packageName: String) {
        closeContextMenu()
        launcherManager.openAppDetails(packageName)
    }

    fun openDefaultLauncherSettings() {
        launcherManager.openDefaultLauncherSettings()
    }

    fun toggleDockPin(app: AppInfo) {
        val currentDock = _uiState.value.dockApps.toMutableList()
        val exists = currentDock.any { it.packageName == app.packageName }
        if (exists) {
            currentDock.removeAll { it.packageName == app.packageName }
        } else {
            if (currentDock.size < _uiState.value.dockAppLimit) {
                currentDock.add(app)
            } else if (currentDock.isNotEmpty()) {
                currentDock.removeAt(0)
                currentDock.add(app)
            } else {
                currentDock.add(app)
            }
        }
        _uiState.value = _uiState.value.copy(dockApps = currentDock)
        closeContextMenu()
    }

    fun toggleFavoriteApp(app: AppInfo) {
        val currentFavs = _uiState.value.favoriteApps.toMutableList()
        val exists = currentFavs.any { it.packageName == app.packageName }
        if (exists) {
            currentFavs.removeAll { it.packageName == app.packageName }
        } else {
            currentFavs.add(app)
        }
        _uiState.value = _uiState.value.copy(favoriteApps = currentFavs)
        closeContextMenu()
    }

    fun setClockSize(sizeSp: Float) {
        _uiState.value = _uiState.value.copy(clockSizeSp = sizeSp.coerceIn(40f, 105f))
    }

    fun setWidgetScale(scale: Float) {
        _uiState.value = _uiState.value.copy(widgetScale = scale.coerceIn(0.8f, 1.25f))
    }

    fun setWidgetShape(shape: WidgetShape) {
        _uiState.value = _uiState.value.copy(widgetShape = shape)
    }

    fun openHomeMenu() {
        _uiState.value = _uiState.value.copy(isHomeMenuOpen = true)
    }

    fun closeHomeMenu() {
        _uiState.value = _uiState.value.copy(isHomeMenuOpen = false)
    }

    fun openAddWidgetSheet() {
        _uiState.value = _uiState.value.copy(isAddWidgetSheetOpen = true, isHomeMenuOpen = false)
    }

    fun closeAddWidgetSheet() {
        _uiState.value = _uiState.value.copy(isAddWidgetSheetOpen = false)
    }

    fun addHomeWidget(type: WidgetType, size: WidgetSize) {
        val currentWidgets = _uiState.value.homeWidgets.toMutableList()
        val existingIndex = currentWidgets.indexOfFirst { it.type == type }
        if (existingIndex >= 0) {
            currentWidgets[existingIndex] = currentWidgets[existingIndex].copy(size = size, isVisible = true)
        } else {
            currentWidgets.add(IndividualWidgetConfig(type = type, size = size))
        }
        _uiState.value = _uiState.value.copy(homeWidgets = currentWidgets)
    }

    fun removeHomeWidget(type: WidgetType) {
        val currentWidgets = _uiState.value.homeWidgets.filter { it.type != type }
        _uiState.value = _uiState.value.copy(
            homeWidgets = currentWidgets,
            selectedIndividualWidgetForEdit = null
        )
    }

    fun updateWidgetSize(type: WidgetType, newSize: WidgetSize) {
        val currentWidgets = _uiState.value.homeWidgets.map {
            if (it.type == type) it.copy(size = newSize) else it
        }
        _uiState.value = _uiState.value.copy(
            homeWidgets = currentWidgets,
            selectedIndividualWidgetForEdit = _uiState.value.selectedIndividualWidgetForEdit?.copy(size = newSize)
        )
    }

    fun updateWidgetShape(type: WidgetType, newShape: WidgetShape) {
        val currentWidgets = _uiState.value.homeWidgets.map {
            if (it.type == type) it.copy(shape = newShape) else it
        }
        _uiState.value = _uiState.value.copy(
            homeWidgets = currentWidgets,
            selectedIndividualWidgetForEdit = _uiState.value.selectedIndividualWidgetForEdit?.copy(shape = newShape)
        )
    }

    fun updateWidgetScale(type: WidgetType, newScale: Float) {
        val currentWidgets = _uiState.value.homeWidgets.map {
            if (it.type == type) it.copy(scale = newScale.coerceIn(0.75f, 1.25f)) else it
        }
        _uiState.value = _uiState.value.copy(
            homeWidgets = currentWidgets,
            selectedIndividualWidgetForEdit = _uiState.value.selectedIndividualWidgetForEdit?.copy(scale = newScale)
        )
    }

    fun openIndividualWidgetEditDialog(config: IndividualWidgetConfig) {
        _uiState.value = _uiState.value.copy(selectedIndividualWidgetForEdit = config)
    }

    fun closeIndividualWidgetEditDialog() {
        _uiState.value = _uiState.value.copy(selectedIndividualWidgetForEdit = null)
    }

    fun openAppRemoveOrDeleteDialog(app: AppInfo) {
        _uiState.value = _uiState.value.copy(appToRemoveOrDelete = app)
    }

    fun closeAppRemoveOrDeleteDialog() {
        _uiState.value = _uiState.value.copy(appToRemoveOrDelete = null)
    }

    fun removeFromHomeScreen(app: AppInfo) {
        val currentFavs = _uiState.value.favoriteApps.filter { it.packageName != app.packageName }
        _uiState.value = _uiState.value.copy(favoriteApps = currentFavs)
        closeAppRemoveOrDeleteDialog()
        closeContextMenu()
    }

    fun uninstallApp(app: AppInfo) {
        launcherManager.uninstallApp(app.packageName)
        val currentFavs = _uiState.value.favoriteApps.filter { it.packageName != app.packageName }
        val currentDock = _uiState.value.dockApps.filter { it.packageName != app.packageName }
        val currentAll = _uiState.value.allApps.filter { it.packageName != app.packageName }
        _uiState.value = _uiState.value.copy(
            favoriteApps = currentFavs,
            dockApps = currentDock,
            allApps = currentAll
        )
        closeAppRemoveOrDeleteDialog()
        closeContextMenu()
    }

    fun openWidgetEditDialog(widgetKey: String) {
        _uiState.value = _uiState.value.copy(selectedWidgetForEdit = widgetKey)
    }

    fun closeWidgetEditDialog() {
        _uiState.value = _uiState.value.copy(selectedWidgetForEdit = null)
    }

    fun toggleEditMode() {
        _uiState.value = _uiState.value.copy(isEditMode = !_uiState.value.isEditMode)
    }

    fun setEditMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isEditMode = enabled)
    }

    fun reorderApps(fromIndex: Int, toIndex: Int) {
        val currentList = if (_uiState.value.showFavoritesShelf && _uiState.value.favoriteApps.isNotEmpty()) {
            _uiState.value.favoriteApps.toMutableList()
        } else {
            _uiState.value.allApps.toMutableList()
        }

        if (fromIndex in currentList.indices && toIndex in currentList.indices && fromIndex != toIndex) {
            val movedItem = currentList.removeAt(fromIndex)
            currentList.add(toIndex, movedItem)

            if (_uiState.value.showFavoritesShelf && _uiState.value.favoriteApps.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(favoriteApps = currentList)
            } else {
                _uiState.value = _uiState.value.copy(allApps = currentList)
            }
        }
    }

    fun moveAppToPosition(app: AppInfo, targetIndex: Int) {
        val currentList = if (_uiState.value.showFavoritesShelf && _uiState.value.favoriteApps.isNotEmpty()) {
            _uiState.value.favoriteApps.toMutableList()
        } else {
            _uiState.value.allApps.toMutableList()
        }

        val currentIndex = currentList.indexOfFirst { it.packageName == app.packageName }
        if (currentIndex != -1) {
            val validTarget = targetIndex.coerceIn(0, currentList.size - 1)
            val moved = currentList.removeAt(currentIndex)
            currentList.add(validTarget, moved)

            if (_uiState.value.showFavoritesShelf && _uiState.value.favoriteApps.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(favoriteApps = currentList)
            } else {
                _uiState.value = _uiState.value.copy(allApps = currentList)
            }
        }
    }

    fun setClockFontOption(option: ClockFontOption) {
        _uiState.value = _uiState.value.copy(clockFontOption = option)
    }

    fun setDockAppLimit(limit: Int) {
        val safeLimit = limit.coerceIn(1, 4)
        val trimmedDock = _uiState.value.dockApps.take(safeLimit)
        _uiState.value = _uiState.value.copy(
            dockAppLimit = safeLimit,
            dockApps = trimmedDock
        )
    }

    fun setGridColumns(columns: Int) {
        _uiState.value = _uiState.value.copy(gridColumns = columns.coerceIn(3, 5))
    }

    fun openDrawer() {
        _uiState.value = _uiState.value.copy(isDrawerOpen = true)
    }

    fun closeDrawer() {
        _uiState.value = _uiState.value.copy(isDrawerOpen = false, searchQuery = "")
        applyFilter()
    }

    fun openSettings() {
        _uiState.value = _uiState.value.copy(isSettingsOpen = true)
    }

    fun closeSettings() {
        _uiState.value = _uiState.value.copy(isSettingsOpen = false)
    }

    fun openContextMenu(app: AppInfo) {
        _uiState.value = _uiState.value.copy(activeContextMenuApp = app)
    }

    fun closeContextMenu() {
        _uiState.value = _uiState.value.copy(activeContextMenuApp = null)
    }

    fun setIconShape(shape: IconShape) {
        _uiState.value = _uiState.value.copy(iconShape = shape)
    }

    fun setIconThemePack(pack: IconThemePack) {
        _uiState.value = _uiState.value.copy(iconThemePack = pack)
    }

    fun setIconSize(size: Float) {
        _uiState.value = _uiState.value.copy(iconSize = size)
    }

    fun setShowLabels(show: Boolean) {
        _uiState.value = _uiState.value.copy(showLabels = show)
    }

    fun setWallpaper(wallpaper: LiquidWallpaperType) {
        val config = LiquidWallpaperConfig.defaultFor(wallpaper)
        _uiState.value = _uiState.value.copy(
            selectedWallpaper = wallpaper,
            wallpaperConfig = config
        )
    }

    fun openWallpaperManager() {
        _uiState.value = _uiState.value.copy(isWallpaperManagerOpen = true)
    }

    fun closeWallpaperManager() {
        _uiState.value = _uiState.value.copy(isWallpaperManagerOpen = false)
    }

    fun applyWallpaperConfig(config: LiquidWallpaperConfig) {
        _uiState.value = _uiState.value.copy(
            wallpaperConfig = config,
            selectedWallpaper = config.wallpaperType,
            isWallpaperManagerOpen = false
        )
    }

    fun applySystemWallpaper(context: android.content.Context, config: LiquidWallpaperConfig, target: SystemWallpaperTarget) {
        viewModelScope.launch {
            SystemWallpaperManager.applyAsSystemWallpaper(context, config, target)
        }
    }

    fun launchLiveWallpaperPreview(context: android.content.Context, config: LiquidWallpaperConfig) {
        SystemWallpaperManager.launchLiveWallpaperPreview(context, config)
    }

    fun setAsDefaultLauncher(context: android.content.Context) {
        SystemWallpaperManager.openDefaultHomeSettings(context)
    }

    fun randomizeWallpaper() {
        val randomConfig = LiquidWallpaperConfig.generateRandomOrganic()
        _uiState.value = _uiState.value.copy(
            wallpaperConfig = randomConfig,
            selectedWallpaper = randomConfig.wallpaperType
        )
    }

    fun resetWallpaperConfig() {
        val currentType = _uiState.value.selectedWallpaper
        val defaultConfig = LiquidWallpaperConfig.defaultFor(currentType)
        _uiState.value = _uiState.value.copy(wallpaperConfig = defaultConfig)
    }

    fun setPerformanceMode(mode: PerformanceMode) {
        _uiState.value = _uiState.value.copy(performanceMode = mode)
    }

    fun toggleWaterRipple() {
        _uiState.value = _uiState.value.copy(waterRippleEnabled = !_uiState.value.waterRippleEnabled)
    }

    fun toggleEcoMode() {
        val nextMode = if (_uiState.value.performanceMode == PerformanceMode.ECO_SAVER) {
            PerformanceMode.AQUA_FLOW_120
        } else {
            PerformanceMode.ECO_SAVER
        }
        setPerformanceMode(nextMode)
    }

    fun refreshWeather() {
        val conditions = listOf(
            "Açık Su Damlacıklı",
            "Işıltılı Sisli",
            "Hafif Yağmurlu",
            "Biyolüminesans Gece",
            "Berrak Gökyüzü"
        )
        val current = _weatherState.value
        val nextCond = conditions.filter { it != current.condition }.random()
        val nextTemp = (20..28).random()
        _weatherState.value = current.copy(
            temperatureCelsius = nextTemp,
            condition = nextCond,
            humidityPercent = (50..85).random()
        )
    }

    private fun observeFlashlight() {
        viewModelScope.launch {
            DeviceFlashlightManager.isTorchOn.collect { torchOn ->
                _uiState.value = _uiState.value.copy(isTorchOn = torchOn)
            }
        }
    }

    fun toggleTorch(context: Context) {
        DeviceFlashlightManager.toggleTorch(context)
    }

    fun toggleMusicReactive() {
        val newState = MusicVisualizerManager.toggleMusicReactive()
        _uiState.value = _uiState.value.copy(isMusicReactive = newState)
    }

    fun setFont(font: LauncherFont) {
        _uiState.value = _uiState.value.copy(selectedFont = font)
    }

    fun toggleClockWidget() {
        _uiState.value = _uiState.value.copy(showClockWidget = !_uiState.value.showClockWidget)
    }

    fun toggleWeatherWidget() {
        _uiState.value = _uiState.value.copy(showWeatherWidget = !_uiState.value.showWeatherWidget)
    }

    fun toggleBatteryWidget() {
        _uiState.value = _uiState.value.copy(showBatteryWidget = !_uiState.value.showBatteryWidget)
    }

    fun toggleMediaWidget() {
        _uiState.value = _uiState.value.copy(showMediaWidget = !_uiState.value.showMediaWidget)
    }

    fun toggleFlashlightQuickAction() {
        _uiState.value = _uiState.value.copy(showFlashlightQuickAction = !_uiState.value.showFlashlightQuickAction)
    }

    fun toggleFavoritesShelf() {
        _uiState.value = _uiState.value.copy(showFavoritesShelf = !_uiState.value.showFavoritesShelf)
    }

    fun toggleSearchBar() {
        _uiState.value = _uiState.value.copy(showSearchBar = !_uiState.value.showSearchBar)
    }

    fun openCustomizeSheet() {
        _uiState.value = _uiState.value.copy(isCustomizeSheetOpen = true)
    }

    fun closeCustomizeSheet() {
        _uiState.value = _uiState.value.copy(isCustomizeSheetOpen = false)
    }

    fun openOnlineWallpaperSheet() {
        _uiState.value = _uiState.value.copy(isOnlineWallpaperSheetOpen = true)
        if (_uiState.value.onlineWallpapers.isEmpty()) {
            loadOnlineWallpapers(_uiState.value.selectedWallpaperCategory)
        }
    }

    fun closeOnlineWallpaperSheet() {
        _uiState.value = _uiState.value.copy(isOnlineWallpaperSheetOpen = false, previewWallpaper = null)
    }

    fun loadOnlineWallpapers(category: WallpaperCategory) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingWallpapers = true,
                selectedWallpaperCategory = category
            )
            val list = OnlineWallpaperRepository.fetchWallpapers(category)
            _uiState.value = _uiState.value.copy(
                onlineWallpapers = list,
                isLoadingWallpapers = false
            )
        }
    }

    fun setPreviewWallpaper(wallpaper: OnlineWallpaper?) {
        _uiState.value = _uiState.value.copy(previewWallpaper = wallpaper)
    }

    fun applyOnlineWallpaper(context: Context, wallpaper: OnlineWallpaper, target: SystemWallpaperTarget) {
        viewModelScope.launch {
            OnlineWallpaperRepository.downloadAndApplyWallpaper(context, wallpaper, target)
        }
    }

    fun applyGalleryWallpaper(context: Context, uri: Uri, target: SystemWallpaperTarget) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(customGalleryWallpaperUri = uri.toString())
            OnlineWallpaperRepository.applyGalleryImageAsWallpaper(context, uri, target)
        }
    }

    fun setCustomGalleryWallpaper(uri: Uri?) {
        _uiState.value = _uiState.value.copy(customGalleryWallpaperUri = uri?.toString())
    }

    fun setIosWallpaperPreset(preset: IosWallpaperPreset) {
        _uiState.value = _uiState.value.copy(
            iosWallpaperPreset = preset,
            customGalleryWallpaperUri = null // Reset custom image when user selects a preset
        )
    }

    fun clearCustomGalleryWallpaper() {
        _uiState.value = _uiState.value.copy(customGalleryWallpaperUri = null)
    }

    fun openDefaultLauncherSettings(context: Context) {
        SystemWallpaperManager.openDefaultHomeSettings(context)
    }

    fun uninstallApp(packageName: String) {
        closeContextMenu()
        launcherManager.uninstallApp(packageName)
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            refreshApps()
        }
    }
}
