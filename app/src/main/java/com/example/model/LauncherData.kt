package com.example.model

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color

/**
 * Information representing an installed or system app.
 */
data class AppInfo(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable? = null,
    val category: AppCategory = AppCategory.ALL,
    val isFavorite: Boolean = false,
    val isPinnedToDock: Boolean = false,
    val installTime: Long = 0L
)

enum class AppCategory(val titleTr: String) {
    ALL("Tümü"),
    ESSENTIALS("Önemli"),
    SOCIAL("Sosyal"),
    MEDIA("Medya"),
    TOOLS("Araçlar"),
    GAMES("Oyunlar"),
    SYSTEM("Sistem")
}

/**
 * Geometric shape for app icons with liquid styling.
 */
enum class IconShape(val titleTr: String) {
    LIQUID_PEBBLE("Sıvı Çakıl"),
    DROPLET("Su Damlası"),
    SQUIRCLE("Yumuşak Kare"),
    GLASS_ORB("Cam Küre"),
    HEXA_AQUA("Hekza Kristal")
}

/**
 * Custom glass theme packs for icons.
 */
enum class IconThemePack(
    val titleTr: String,
    val primaryTint: Color,
    val secondaryTint: Color,
    val glowColor: Color,
    val description: String
) {
    AURA_CYAN(
        "Aura Cyan",
        Color(0xFF00F0FF),
        Color(0xFF0077B6),
        Color(0x6000E5FF),
        "Turkuaz su kırılmaları ve parlak sıvı cam yüzey"
    ),
    BIOLUMINESCENT(
        "Derin Okyanus",
        Color(0xFF00F5A0),
        Color(0xFF00D9F5),
        Color(0x6000F5A0),
        "Karanlık sularda parlayan biyolüminesans ışıma"
    ),
    FROSTED_ICE(
        "Buz Kristali",
        Color(0xFFE0F7FA),
        Color(0xFF80DEEA),
        Color(0x50FFFFFF),
        "Kutup beyazı ve saydam buzlu cam efekti"
    ),
    SOLAR_AMBER(
        "Güneş Nektarı",
        Color(0xFFFFB703),
        Color(0xFFFB8500),
        Color(0x60FFB703),
        "Sıvı altın ve amber ışıltılı sıcak cam tonları"
    ),
    OBSIDIAN_WATER(
        "Obsidiyen Sıvı",
        Color(0xFF8A99AD),
        Color(0xFF485563),
        Color(0x5000F5FF),
        "Karartılmış füme cam ve gizli su yansımaları"
    ),
    SYSTEM_ORIGINAL(
        "Doğal + Cam Halka",
        Color(0xFF00F0FF),
        Color(0xFF7209B7),
        Color(0x4000F0FF),
        "Orijinal uygulama simgelerine sıvı cam kaide"
    )
}

/**
 * Performance & battery engine profiles.
 */
enum class PerformanceMode(
    val titleTr: String,
    val description: String,
    val fpsLimit: Int,
    val causticsEnabled: Boolean,
    val rippleEnabled: Boolean
) {
    AQUA_FLOW_120(
        "Aqua Flow (120 Hz)",
        "En yüksek akıcılık, tam sıvı fiziği ve su dalgalanma efektleri",
        120,
        true,
        true
    ),
    BALANCED_60(
        "Dengeli (60 Hz)",
        "Akıcı geçişler ve optimize edilmiş pil tasarrufu",
        60,
        true,
        true
    ),
    ECO_SAVER(
        "Derin Pil Tasarrufu",
        "Arka plan shader hesaplarını kısarak maksimum batarya ömrü sağlar",
        45,
        false,
        false
    )
}

/**
 * Built-in dynamic liquid wallpapers.
 */
enum class LiquidWallpaperType(
    val titleTr: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color
) {
    HYDRA_ABYSS(
        "Hydra Derinliği",
        Color(0xFF040D1A),
        Color(0xFF0A2239),
        Color(0xFF00F0FF)
    ),
    NEON_AURORA(
        "Sıvı Aurora",
        Color(0xFF0A0E2A),
        Color(0xFF1E103A),
        Color(0xFF00F5D4)
    ),
    BIOLUMINESCENT_REEF(
        "Okyanus Mercanı",
        Color(0xFF021B1A),
        Color(0xFF053835),
        Color(0xFF00FFC2)
    ),
    AMETHYST_FLUID(
        "Ametist Damlası",
        Color(0xFF160926),
        Color(0xFF2D144A),
        Color(0xFFBF55EC)
    ),
    SUNSET_NECTAR(
        "Amber Sıvısı",
        Color(0xFF1C0D02),
        Color(0xFF381A05),
        Color(0xFFFF9E00)
    )
}

data class BatteryState(
    val levelPercent: Int = 85,
    val isCharging: Boolean = false,
    val temperatureCelsius: Float = 28.5f,
    val health: String = "Harika",
    val voltageMv: Int = 4150
)

data class WeatherState(
    val city: String = "İstanbul",
    val temperatureCelsius: Int = 22,
    val condition: String = "Açık Su Damlacıklı",
    val humidityPercent: Int = 62,
    val windSpeedKmh: Int = 14,
    val airQualityIndex: Int = 34
)
