package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    OUTFIT("Outfit (Modern iOS Minimal)"),
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
