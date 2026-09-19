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
 * Geometric shape for Home widgets (Weather, Battery, Clock, Media, etc.).
 */
enum class WidgetShape(val titleTr: String, val cornerRadiusPercent: Float) {
    ROUNDED_SQUIRCLE("Köşeli Yumuşak", 22f),
    PILL("Kapsül", 36f),
    SHARP_MODERN("Keskin Modern", 10f),
    CIRCLE("Dairesel / Oval", 50f)
}

/**
 * Geometric shape options specifically for App Folders (Klasörler).
 */
enum class FolderShape(val titleTr: String, val cornerRadiusDp: Float) {
    SQUIRCLE("Yumuşak Kare", 22f),
    PILL("Kapsül / Yuvarlak", 32f),
    SHARP("Keskin Modern", 10f),
    CIRCLE("Dairesel", 40f)
}

/**
 * Grid layout columns for expanded folder view.
 */
enum class FolderGridColumns(val titleTr: String, val columns: Int) {
    THREE("3x3 Izgara", 3),
    FOUR("4x4 Izgara", 4),
    TWO("2x2 Kompakt", 2)
}

/**
 * Configuration for folder appearance & inner grid layout.
 */
data class FolderConfig(
    val shape: FolderShape = FolderShape.SQUIRCLE,
    val opacity: Float = 0.85f, // 0.20f (transparent) to 1.0f (solid matte)
    val gridColumns: FolderGridColumns = FolderGridColumns.FOUR,
    val blurIntensity: Float = 1.0f
)

/**
 * Dimension / aspect size for individual widgets.
 */
enum class WidgetSize(val titleTr: String, val spanColumns: Int) {
    SQUARE("Kare (2x2)", 2),
    HORIZONTAL("Yatay Dikdörtgen (4x2)", 4),
    VERTICAL("Dikey Dikdörtgen (2x4)", 2),
    COMPACT("Kompakt", 2)
}

/**
 * Available widget types in the launcher (+6 distinct widgets).
 */
enum class WidgetType(val id: String, val titleTr: String, val subtitleTr: String, val defaultSize: WidgetSize) {
    CLOCK("clock", "Saat & Tarih", "Modern minimalist saat, tarih ve saniye", WidgetSize.HORIZONTAL),
    WEATHER("weather", "Hava Durumu", "Gerçek zamanlı sıcaklık, tahmin ve nem", WidgetSize.SQUARE),
    BATTERY("battery", "Pil & Enerji", "Pil seviyesi, şarj durumu ve eko modu", WidgetSize.SQUARE),
    MEDIA("media", "Müzik Çalar", "Çalan parça, albüm kapağı ve kontroller", WidgetSize.HORIZONTAL),
    CONTROLS("controls", "Hızlı Kontroller", "Fener, Wi-Fi, Bluetooth ve ses karoları", WidgetSize.SQUARE),
    CALENDAR("calendar", "Ajanda & Takvim", "Günün tarihi ve haftalık takvim şeridi", WidgetSize.HORIZONTAL),
    NOTES("notes", "Günün Notu & Sözü", "Hızlı notlar ve motivasyon kartı", WidgetSize.HORIZONTAL),
    SPEED_DIAL("speed_dial", "Hızlı Arama", "Sık aranan kişiler ve rehber kısayolu", WidgetSize.SQUARE)
}

/**
 * Individual widget configuration. Each widget has its own size, shape, scale, and visibility.
 */
data class IndividualWidgetConfig(
    val type: WidgetType,
    val size: WidgetSize = type.defaultSize,
    val shape: WidgetShape = WidgetShape.ROUNDED_SQUIRCLE,
    val scale: Float = 1.0f,
    val horizontalScale: Float = 1.0f,
    val verticalScale: Float = 1.0f,
    val isVisible: Boolean = true
)

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
    val accentColor: Color,
    val subtitleTr: String = "Sıvı Cam Efekti",
    val description: String = "Organik su ve cam optiği kırılımları"
) {
    HYDRA_ABYSS(
        "Hydra Derinliği",
        Color(0xFF040D1A),
        Color(0xFF0A2239),
        Color(0xFF00F0FF),
        "Derin Okyanus & Cam",
        "Karanlık okyanus derinliklerinde turkuaz su kostikleri ve cam yansımaları"
    ),
    PRISMATIC_CRYSTAL(
        "Prizmatik Kristal",
        Color(0xFF0C1024),
        Color(0xFF1C2248),
        Color(0xFF80D8FF),
        "Gökkuşağı Kırılmaları",
        "Yüksek kırılma indeksli sıvı kuvars ve spektral renk ayrışması"
    ),
    NEON_AURORA(
        "Sıvı Aurora",
        Color(0xFF0A0E2A),
        Color(0xFF1E103A),
        Color(0xFF00F5D4),
        "Kutup Işıkları & Plazma",
        "Menekşe ve elektrik yeşili akışkan dalgalar ve yumuşak ışık kırılımları"
    ),
    CYBER_AQUA(
        "Siber Akua",
        Color(0xFF021324),
        Color(0xFF072D4A),
        Color(0xFF00E5FF),
        "Gelecekçi Cam Matrisi",
        "Yüksek kontrastlı neon cam kostikleri ve keskin sıvı kenar parlamaları"
    ),
    BIOLUMINESCENT_REEF(
        "Okyanus Mercanı",
        Color(0xFF021B1A),
        Color(0xFF053835),
        Color(0xFF00FFC2),
        "Sualtı Biyolüminesansı",
        "Zümrüt yeşili derin sular, fosforlu planktonlar ve yumuşak su dalgaları"
    ),
    AMETHYST_FLUID(
        "Ametist Damlası",
        Color(0xFF160926),
        Color(0xFF2D144A),
        Color(0xFFBF55EC),
        "Mor Kristal Sıvısı",
        "Zengin ametist moru tonlarında parlak cam akışkanlığı"
    ),
    SUNSET_NECTAR(
        "Amber Sıvısı",
        Color(0xFF1C0D02),
        Color(0xFF381A05),
        Color(0xFFFF9E00),
        "Güneş Sıcaklığı",
        "Altın bal tonlarında ılık güneş ışığı ve yumuşak sıvı cam kırınımı"
    ),
    OBSIDIAN_MIRROR(
        "Obsidiyen Ayna",
        Color(0xFF080B10),
        Color(0xFF141922),
        Color(0xFF90A4AE),
        "Füme Kristal Yansıma",
        "Minimalist koyu tonlu sıvı cam ayna ve gümüşî kostik parıltıları"
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
