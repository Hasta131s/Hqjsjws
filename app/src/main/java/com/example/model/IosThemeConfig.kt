package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Modern Minimalist Theme & Wallpaper presets (No branded names).
 */
enum class IosWallpaperPreset(
    val titleTr: String,
    val subtitleTr: String,
    val topColor: Color,
    val midColor: Color,
    val bottomColor: Color,
    val accentColor: Color
) {
    IOS_18_NEBULA(
        "Kozmik İpek",
        "Zengin Mor Işıltı",
        Color(0xFF130626),
        Color(0xFF2B0E52),
        Color(0xFF05020B),
        Color(0xFFD0BCFF)
    ),
    IOS_18_TITANIUM_DESERT(
        "Çöl Titanyumu",
        "Sıcak Doğal Titanyum",
        Color(0xFF21150D),
        Color(0xFF3D2719),
        Color(0xFF0B0704),
        Color(0xFFE4B382)
    ),
    IOS_18_DEEP_CYAN(
        "Okyanus Mavisi",
        "Derin Gece Işıltısı",
        Color(0xFF031627),
        Color(0xFF093154),
        Color(0xFF010912),
        Color(0xFF56CCF2)
    ),
    IOS_17_PRISM_LIGHT(
        "Prizma Işığı",
        "Spektral Şerit",
        Color(0xFF0C0E28),
        Color(0xFF29153E),
        Color(0xFF03040E),
        Color(0xFFFF6584)
    ),
    IOS_ASTRONOMY_EARTH(
        "Astronomi",
        "Derin Uzay & Atmosfer",
        Color(0xFF000713),
        Color(0xFF0A1E36),
        Color(0xFF000207),
        Color(0xFF80D8FF)
    ),
    IOS_OLED_MIDNIGHT(
        "Saf Gece OLED",
        "Ultra Minimalist Saf Siyah",
        Color(0xFF000000),
        Color(0xFF0A0C10),
        Color(0xFF000000),
        Color(0xFFAAAAAA)
    )
}

/**
 * Icon Style options.
 */
enum class IosIconStyle(val titleTr: String) {
    IOS_CLASSIC("Klasik Simgeler"),
    IOS_DARK_TINTED("Koyu & Renkli"),
    IOS_MINIMAL_MONO("Monokrom Minimal")
}
