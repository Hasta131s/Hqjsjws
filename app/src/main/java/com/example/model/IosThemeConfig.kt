package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * ColorOS & Minimalist UI Theme Mode (Siyah / Beyaz / Sistem).
 */
enum class LauncherThemeMode(val titleTr: String, val subtitleTr: String) {
    DARK_AMOLED("Koyu (Siyah OLED)", "Derin siyah, düşük opaklık ve yüksek kontrast"),
    LIGHT_PEARL("Açık (Beyaz İnci)", "Ferah beyaz, ipeksi buzlu cam ve berrak görünüm"),
    SYSTEM_AUTO("Sistem Otomatik", "Cihaz temasına göre otomatik siyah/beyaz")
}

/**
 * Modern Minimalist & ColorOS Theme Wallpaper presets.
 */
enum class IosWallpaperPreset(
    val titleTr: String,
    val subtitleTr: String,
    val topColor: Color,
    val midColor: Color,
    val bottomColor: Color,
    val accentColor: Color
) {
    COLOR_OS_AQUAMORPHIC(
        "ColorOS Akua",
        "Akışkan Su & Derin Cam",
        Color(0xFF021B2B),
        Color(0xFF0A3A54),
        Color(0xFF03101C),
        Color(0xFF00D2FF)
    ),
    COLOR_OS_PEARL_WHITE(
        "ColorOS Saf Beyaz",
        "İpeksi Buzlu Beyaz & İnci",
        Color(0xFFF0F4F8),
        Color(0xFFE2E8F0),
        Color(0xFFD6DFE8),
        Color(0xFF007AFF)
    ),
    IOS_OLED_MIDNIGHT(
        "Saf Siyah OLED",
        "Ultra Minimalist Siyah",
        Color(0xFF000000),
        Color(0xFF08090C),
        Color(0xFF000000),
        Color(0xFF38BDF8)
    ),
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
