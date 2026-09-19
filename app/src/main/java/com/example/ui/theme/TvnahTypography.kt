package com.example.ui.theme

import android.graphics.Typeface
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.R

val OutfitFontFamily = FontFamily(
    Font(R.font.outfit, FontWeight.Normal),
    Font(R.font.outfit, FontWeight.Medium),
    Font(R.font.outfit, FontWeight.SemiBold),
    Font(R.font.outfit, FontWeight.Bold)
)

val PlusJakartaFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans, FontWeight.Bold)
)

enum class LauncherFont(val titleTr: String) {
    OUTFIT("Outfit (Modern Geometrik)"),
    PLUS_JAKARTA("Plus Jakarta Sans (Akıcı Şık)"),
    SYSTEM("Sistem Varsayılanı")
}

fun getLauncherFontFamily(font: LauncherFont): FontFamily {
    return when (font) {
        LauncherFont.OUTFIT -> OutfitFontFamily
        LauncherFont.PLUS_JAKARTA -> PlusJakartaFontFamily
        LauncherFont.SYSTEM -> FontFamily.Default
    }
}

/**
 * 20 Distinctive Clock Font Choices for user customization.
 */
enum class ClockFontOption(val displayName: String) {
    OUTFIT_BOLD("1. Outfit Bold (Modern)"),
    JAKARTA_SEMI("2. Plus Jakarta Sans"),
    SYSTEM_BOLD("3. Standart Kalın"),
    SYSTEM_LIGHT("4. Ultra İnce Minimal"),
    CONDENSED_TALL("5. Uzun Dar Poster"),
    CONDENSED_BOLD("6. Dar Kalın Manşet"),
    MONO_DIGITAL("7. Monospace Terminal"),
    MONO_BOLD("8. Kalın Kod Monospace"),
    SERIF_ROMAN("9. Klasik Serif Roma"),
    SERIF_BOLD("10. Kalın Kitap Serif"),
    SERIF_ITALIC("11. Eğik Zarif Serif"),
    CASUAL_ROUND("12. Yuvarlak Hatlı Casual"),
    CURSIVE_SCRIPT("13. Akıcı El Yazısı"),
    TECH_CLEAN("14. Siber Geometrik"),
    HEAVY_BLACK("15. Süper Kalın (Heavy)"),
    MEDIUM_SANS("16. Denge Medium Sans"),
    EDITORIAL("17. Editoryal Tipografi"),
    VINTAGE_TYPE("18. Retro Daktilo"),
    WIDE_TRACKING("19. Geniş Karakter Aralığı"),
    NORDIC_MINIMAL("20. Nordik Minimalist")
}

fun getClockFontFamily(option: ClockFontOption): FontFamily {
    return when (option) {
        ClockFontOption.OUTFIT_BOLD -> OutfitFontFamily
        ClockFontOption.JAKARTA_SEMI -> PlusJakartaFontFamily
        ClockFontOption.SYSTEM_BOLD -> FontFamily.Default
        ClockFontOption.SYSTEM_LIGHT -> FontFamily(Typeface.create("sans-serif-light", Typeface.NORMAL))
        ClockFontOption.CONDENSED_TALL -> FontFamily(Typeface.create("sans-serif-condensed", Typeface.NORMAL))
        ClockFontOption.CONDENSED_BOLD -> FontFamily(Typeface.create("sans-serif-condensed", Typeface.BOLD))
        ClockFontOption.MONO_DIGITAL -> FontFamily.Monospace
        ClockFontOption.MONO_BOLD -> FontFamily(Typeface.create("monospace", Typeface.BOLD))
        ClockFontOption.SERIF_ROMAN -> FontFamily.Serif
        ClockFontOption.SERIF_BOLD -> FontFamily(Typeface.create("serif", Typeface.BOLD))
        ClockFontOption.SERIF_ITALIC -> FontFamily(Typeface.create("serif", Typeface.ITALIC))
        ClockFontOption.CASUAL_ROUND -> FontFamily(Typeface.create("casual", Typeface.NORMAL))
        ClockFontOption.CURSIVE_SCRIPT -> FontFamily.Cursive
        ClockFontOption.TECH_CLEAN -> FontFamily(Typeface.create("sans-serif-medium", Typeface.NORMAL))
        ClockFontOption.HEAVY_BLACK -> FontFamily(Typeface.create("sans-serif-black", Typeface.BOLD))
        ClockFontOption.MEDIUM_SANS -> FontFamily(Typeface.create("sans-serif-medium", Typeface.BOLD))
        ClockFontOption.EDITORIAL -> FontFamily(Typeface.create("serif-monospace", Typeface.NORMAL))
        ClockFontOption.VINTAGE_TYPE -> FontFamily(Typeface.create("serif-monospace", Typeface.BOLD))
        ClockFontOption.WIDE_TRACKING -> FontFamily(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL))
        ClockFontOption.NORDIC_MINIMAL -> FontFamily(Typeface.create("sans-serif-thin", Typeface.NORMAL))
    }
}

fun getClockFontWeight(option: ClockFontOption): FontWeight {
    return when (option) {
        ClockFontOption.OUTFIT_BOLD -> FontWeight.Bold
        ClockFontOption.JAKARTA_SEMI -> FontWeight.SemiBold
        ClockFontOption.SYSTEM_BOLD -> FontWeight.Bold
        ClockFontOption.SYSTEM_LIGHT -> FontWeight.Light
        ClockFontOption.CONDENSED_TALL -> FontWeight.Normal
        ClockFontOption.CONDENSED_BOLD -> FontWeight.Bold
        ClockFontOption.MONO_DIGITAL -> FontWeight.Medium
        ClockFontOption.MONO_BOLD -> FontWeight.Bold
        ClockFontOption.SERIF_ROMAN -> FontWeight.Normal
        ClockFontOption.SERIF_BOLD -> FontWeight.Bold
        ClockFontOption.SERIF_ITALIC -> FontWeight.Medium
        ClockFontOption.CASUAL_ROUND -> FontWeight.Normal
        ClockFontOption.CURSIVE_SCRIPT -> FontWeight.Normal
        ClockFontOption.TECH_CLEAN -> FontWeight.Medium
        ClockFontOption.HEAVY_BLACK -> FontWeight.Black
        ClockFontOption.MEDIUM_SANS -> FontWeight.Medium
        ClockFontOption.EDITORIAL -> FontWeight.Normal
        ClockFontOption.VINTAGE_TYPE -> FontWeight.Normal
        ClockFontOption.WIDE_TRACKING -> FontWeight.Normal
        ClockFontOption.NORDIC_MINIMAL -> FontWeight.Light
    }
}
