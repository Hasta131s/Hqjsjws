package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.AppLauncherManager
import com.example.engine.BatteryMonitor
import com.example.model.AppCategory
import com.example.model.AppInfo
import com.example.model.BatteryState
import com.example.model.IconShape
import com.example.model.IconThemePack
import com.example.model.LiquidWallpaperType
import com.example.model.PerformanceMode
import com.example.model.WeatherState
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
    val iconShape: IconShape = IconShape.LIQUID_PEBBLE,
    val iconThemePack: IconThemePack = IconThemePack.AURA_CYAN,
    val iconSize: Float = 56f,
    val showLabels: Boolean = true,
    val selectedWallpaper: LiquidWallpaperType = LiquidWallpaperType.HYDRA_ABYSS,
    val performanceMode: PerformanceMode = PerformanceMode.AQUA_FLOW_120,
    val waterRippleEnabled: Boolean = true,
    val isDrawerOpen: Boolean = false,
    val isSettingsOpen: Boolean = false,
    val activeContextMenuApp: AppInfo? = null,
    val isLoadingApps: Boolean = true
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
            if (currentDock.size < 5) {
                currentDock.add(app)
            }
        }
        _uiState.value = _uiState.value.copy(dockApps = currentDock)
        closeContextMenu()
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
        _uiState.value = _uiState.value.copy(selectedWallpaper = wallpaper)
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
}
