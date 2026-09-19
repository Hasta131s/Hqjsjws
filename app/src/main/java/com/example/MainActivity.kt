package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.home.HomeScreen
import com.example.ui.liquid.LiquidGlassTheme
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.LauncherViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme(darkTheme = true) {
        val launcherViewModel: LauncherViewModel = viewModel()
        val uiState by launcherViewModel.uiState.collectAsState()

        // Launcher Back Button handling:
        // Close wallpaper manager, drawer or settings first before anything else
        BackHandler(enabled = uiState.isDrawerOpen || uiState.isSettingsOpen || uiState.isWallpaperManagerOpen) {
          if (uiState.isWallpaperManagerOpen) {
            launcherViewModel.closeWallpaperManager()
          } else if (uiState.isSettingsOpen) {
            launcherViewModel.closeSettings()
          } else if (uiState.isDrawerOpen) {
            launcherViewModel.closeDrawer()
          }
        }

        Surface(
          modifier = Modifier.fillMaxSize(),
          color = LiquidGlassTheme.DeepObsidian
        ) {
          HomeScreen(viewModel = launcherViewModel)
        }
      }
    }
  }
}
