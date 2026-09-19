package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Authentic Apple iOS Theme & Wallpaper presets for 2026 iOS 18/17 style.
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
        "iOS 18 Kozmik İpek",
        "Apple Dark Silk Glow",
        Color(0xFF130626),
        Color(0xFF2B0E52),
        Color(0xFF05020B),
        Color(0xFFD0BCFF)
    ),
    IOS_18_TITANIUM_DESERT(
        "iOS 18 Çöl Titanyumu",
        "iPhone 16 Pro Sıcak Altın",
        Color(0xFF21150D),
        Color(0xFF3D2719),
        Color(0xFF0B0704),
        Color(0xFFE4B382)
    ),
    IOS_18_DEEP_CYAN(
        "iOS 18 Okyanus Mavisi",
        "Derin Gece Işıltısı",
        Color(0xFF031627),
        Color(0xFF093154),
        Color(0xFF010912),
        Color(0xFF56CCF2)
    ),
    IOS_17_PRISM_LIGHT(
        "iOS 17 Prizma Işığı",
        "Apple İridyum Şerit",
        Color(0xFF0C0E28),
        Color(0xFF29153E),
        Color(0xFF03040E),
        Color(0xFFFF6584)
    ),
    IOS_ASTRONOMY_EARTH(
        "iOS Astronomi",
        "Derin Uzay & Atmosfer",
        Color(0xFF000713),
        Color(0xFF0A1E36),
        Color(0xFF000207),
        Color(0xFF80D8FF)
    ),
    IOS_OLED_MIDNIGHT(
        "iOS Saf Gece OLED",
        "Ultra Minimalist Saf Siyah",
        Color(0xFF000000),
        Color(0xFF0A0C10),
        Color(0xFF000000),
        Color(0xFFAAAAAA)
    )
}

/**
 * iOS Icon Style options (iOS 18 Tinted Dark, iOS 18 Light, Original Squircle).
 */
enum class IosIconStyle(val titleTr: String) {
    IOS_CLASSIC("Klasik iOS Simgeleri"),
    IOS_DARK_TINTED("iOS 18 Koyu & Renkli"),
    IOS_MINIMAL_MONO("iOS Monokrom")
}
