package com.example.model

import androidx.compose.ui.graphics.Color

/**
 * Configuration and optics parameters for the Compose Liquid-Glass Dynamic Wallpaper Engine.
 */
data class LiquidWallpaperConfig(
    val wallpaperType: LiquidWallpaperType = LiquidWallpaperType.HYDRA_ABYSS,
    val flowSpeed: Float = 1.0f,               // 0.2f (calm meditative) .. 2.5f (dynamic turbulent flow)
    val glassThickness: Float = 1.2f,          // 0.5f (thin fluid) .. 2.5f (thick refractive glass slab)
    val causticIntensity: Float = 0.65f,       // 0.0f .. 1.0f (specular refraction glare brightness)
    val chromaticAberration: Boolean = true,   // Prismatic spectral dispersion along glass curvature
    val floatingDropletsEnabled: Boolean = true,// Liquid glass metaball droplets floating in fluid orbits
    val dropletCount: Int = 6,                 // 2 .. 10 floating droplets
    val frostedDiffusion: Float = 0.15f,       // 0.0f (ultra crystal) .. 1.0f (frosted diffused glass)
    val interactiveTouchReaction: Boolean = true // Fluid surface ripples when touched
) {
    companion object {
        fun defaultFor(type: LiquidWallpaperType): LiquidWallpaperConfig {
            return when (type) {
                LiquidWallpaperType.HYDRA_ABYSS -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 1.0f,
                    glassThickness = 1.3f,
                    causticIntensity = 0.70f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 6,
                    frostedDiffusion = 0.12f
                )
                LiquidWallpaperType.PRISMATIC_CRYSTAL -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 1.2f,
                    glassThickness = 1.8f,
                    causticIntensity = 0.85f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 8,
                    frostedDiffusion = 0.05f
                )
                LiquidWallpaperType.NEON_AURORA -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 0.85f,
                    glassThickness = 1.4f,
                    causticIntensity = 0.75f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 7,
                    frostedDiffusion = 0.18f
                )
                LiquidWallpaperType.BIOLUMINESCENT_REEF -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 0.75f,
                    glassThickness = 1.1f,
                    causticIntensity = 0.60f,
                    chromaticAberration = false,
                    floatingDropletsEnabled = true,
                    dropletCount = 8,
                    frostedDiffusion = 0.20f
                )
                LiquidWallpaperType.AMETHYST_FLUID -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 1.1f,
                    glassThickness = 1.5f,
                    causticIntensity = 0.70f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 5,
                    frostedDiffusion = 0.10f
                )
                LiquidWallpaperType.SUNSET_NECTAR -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 0.9f,
                    glassThickness = 1.2f,
                    causticIntensity = 0.65f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 5,
                    frostedDiffusion = 0.15f
                )
                LiquidWallpaperType.CYBER_AQUA -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 1.4f,
                    glassThickness = 1.6f,
                    causticIntensity = 0.80f,
                    chromaticAberration = true,
                    floatingDropletsEnabled = true,
                    dropletCount = 7,
                    frostedDiffusion = 0.08f
                )
                LiquidWallpaperType.OBSIDIAN_MIRROR -> LiquidWallpaperConfig(
                    wallpaperType = type,
                    flowSpeed = 0.6f,
                    glassThickness = 1.0f,
                    causticIntensity = 0.45f,
                    chromaticAberration = false,
                    floatingDropletsEnabled = false,
                    dropletCount = 3,
                    frostedDiffusion = 0.25f
                )
            }
        }

        fun generateRandomOrganic(): LiquidWallpaperConfig {
            val randomType = LiquidWallpaperType.values().random()
            return LiquidWallpaperConfig(
                wallpaperType = randomType,
                flowSpeed = (0.5f..2.0f).random(),
                glassThickness = (0.8f..2.2f).random(),
                causticIntensity = (0.4f..0.95f).random(),
                chromaticAberration = listOf(true, true, false).random(),
                floatingDropletsEnabled = true,
                dropletCount = (4..9).random(),
                frostedDiffusion = (0.05f..0.35f).random(),
                interactiveTouchReaction = true
            )
        }

        private fun ClosedFloatingPointRange<Float>.random(): Float {
            return (start + Math.random().toFloat() * (endInclusive - start))
        }
    }
}
