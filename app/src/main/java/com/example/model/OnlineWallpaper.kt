package com.example.model

/**
 * Model representing an online downloadable wallpaper.
 */
data class OnlineWallpaper(
    val id: String,
    val title: String,
    val category: String,
    val author: String,
    val thumbnailUrl: String,
    val downloadUrl: String
)

enum class WallpaperCategory(val titleTr: String, val query: String) {
    ALL("Tümü", ""),
    IOS_MINIMAL("iOS Minimal", "minimal,gradient,ios"),
    NATURE("Doğa & Deniz", "nature,water,landscape"),
    ABSTRACT_LIQUID("Sıvı Cam & 3D", "liquid,fluid,glass"),
    DARK_AMOLED("Karanlık OLED", "dark,night,space"),
    CYBER_NEON("Siber & Neon", "neon,city,cyberpunk")
}
