package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.model.LauncherThemeMode

/**
 * ColorOS Dynamic Theme Palette (Aquamorphic Dark & Pearl Light).
 * Designed for translucent frosted glass, vibrant accents, and high legibility.
 */
data class ColorOsPalette(
    val isDark: Boolean,
    val surfaceColor: Color,
    val surfaceBorderColor: Color,
    val primaryTextColor: Color,
    val secondaryTextColor: Color,
    val tertiaryTextColor: Color,
    val accentColor: Color,
    val cardBackground: Color,
    val searchPillBackground: Color,
    val dockBackground: Color,
    val sheetBackground: Color,
    val iconTint: Color
)

@Composable
fun rememberColorOsPalette(
    themeMode: LauncherThemeMode,
    surfaceOpacity: Float = 0.55f
): ColorOsPalette {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        LauncherThemeMode.DARK_AMOLED -> true
        LauncherThemeMode.LIGHT_PEARL -> false
        LauncherThemeMode.SYSTEM_AUTO -> isSystemDark
    }

    return if (isDark) {
        ColorOsPalette(
            isDark = true,
            surfaceColor = Color(0xFF14171C).copy(alpha = surfaceOpacity),
            surfaceBorderColor = Color.White.copy(alpha = (surfaceOpacity * 0.35f).coerceIn(0.08f, 0.25f)),
            primaryTextColor = Color.White,
            secondaryTextColor = Color.White.copy(alpha = 0.75f),
            tertiaryTextColor = Color.White.copy(alpha = 0.50f),
            accentColor = Color(0xFF00D2FF), // ColorOS Aquamorphic Electric Cyan
            cardBackground = Color(0xFF161920).copy(alpha = surfaceOpacity.coerceIn(0.20f, 0.85f)),
            searchPillBackground = Color(0xFF1C2028).copy(alpha = surfaceOpacity.coerceIn(0.25f, 0.90f)),
            dockBackground = Color(0xFF12151B).copy(alpha = surfaceOpacity.coerceIn(0.35f, 0.90f)),
            sheetBackground = Color(0xFF0C0E12).copy(alpha = 0.96f),
            iconTint = Color.White
        )
    } else {
        ColorOsPalette(
            isDark = false,
            surfaceColor = Color(0xFFFFFFFF).copy(alpha = surfaceOpacity.coerceIn(0.40f, 0.90f)),
            surfaceBorderColor = Color(0xFF000000).copy(alpha = 0.10f),
            primaryTextColor = Color(0xFF111827),
            secondaryTextColor = Color(0xFF4B5563),
            tertiaryTextColor = Color(0xFF9CA3AF),
            accentColor = Color(0xFF0066FF), // ColorOS Rich Ocean Blue
            cardBackground = Color(0xFFF8FAFC).copy(alpha = surfaceOpacity.coerceIn(0.45f, 0.92f)),
            searchPillBackground = Color(0xFFFFFFFF).copy(alpha = surfaceOpacity.coerceIn(0.60f, 0.95f)),
            dockBackground = Color(0xFFFFFFFF).copy(alpha = surfaceOpacity.coerceIn(0.55f, 0.92f)),
            sheetBackground = Color(0xFFF1F5F9).copy(alpha = 0.97f),
            iconTint = Color(0xFF1E293B)
        )
    }
}
