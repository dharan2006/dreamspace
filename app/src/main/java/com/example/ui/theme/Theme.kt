package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

val CosmicColorScheme = darkColorScheme(
    primary = CosmicPrimary,
    onPrimary = CosmicOnPrimary,
    primaryContainer = CosmicPrimaryContainer,
    onPrimaryContainer = CosmicOnPrimaryContainer,
    secondary = CosmicSecondary,
    onSecondary = CosmicOnSecondary,
    secondaryContainer = CosmicSecondaryContainer,
    onSecondaryContainer = CosmicOnSecondaryContainer,
    tertiary = CosmicTertiary,
    onTertiary = CosmicOnTertiary,
    tertiaryContainer = CosmicTertiaryContainer,
    onTertiaryContainer = CosmicOnTertiaryContainer,
    background = CosmicBackground,
    onBackground = CosmicOnSurface,
    surface = CosmicSurface,
    onSurface = CosmicOnSurface,
    surfaceVariant = CosmicSurfaceContainerHighest,
    onSurfaceVariant = CosmicOnSurfaceVariant,
    surfaceContainerLowest = CosmicSurfaceContainerLowest,
    surfaceContainerLow = CosmicSurfaceContainerLow,
    surfaceContainer = CosmicSurfaceContainer,
    surfaceContainerHigh = CosmicSurfaceContainerHigh,
    surfaceContainerHighest = CosmicSurfaceContainerHighest,
    surfaceBright = CosmicSurfaceBright,
    outline = CosmicOutline,
    outlineVariant = CosmicOutlineVariant,
    error = CosmicError,
    onError = CosmicOnError
)

@Composable
fun DreamscapeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = AppTypography,
        content = content
    )
}
