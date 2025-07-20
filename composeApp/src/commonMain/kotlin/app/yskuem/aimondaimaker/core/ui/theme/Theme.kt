package app.yskuem.aimondaimaker.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Premium Light Color Scheme
private val LightColorScheme =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = BackgroundTertiary,
        primaryContainer = BrandPrimaryContainer,
        onPrimaryContainer = BrandPrimaryDark,
        secondary = BrandSecondary,
        onSecondary = BackgroundTertiary,
        secondaryContainer = BrandSecondaryContainer,
        onSecondaryContainer = BrandSecondaryDark,
        tertiary = BrandTertiary,
        onTertiary = BackgroundTertiary,
        tertiaryContainer = BrandTertiaryContainer,
        onTertiaryContainer = BrandTertiaryDark,
        error = ErrorRed,
        onError = BackgroundTertiary,
        errorContainer = ErrorRedContainer,
        onErrorContainer = ErrorRedOnContainer,
        background = BackgroundPrimary,
        onBackground = TextPrimary,
        surface = SurfacePremium,
        onSurface = TextPrimary,
        surfaceVariant = SurfaceContainer,
        onSurfaceVariant = TextSecondary,
        outline = BorderPrimary,
        outlineVariant = OutlineVariant,
        surfaceContainer = SurfaceContainer,
        surfaceContainerLow = SurfaceContainerLow,
        surfaceContainerLowest = BackgroundSecondary,
        surfaceContainerHigh = SurfaceContainerHigh,
        surfaceContainerHighest = SurfaceElevated,
        inverseSurface = TextPrimary,
        inverseOnSurface = BackgroundTertiary,
        inversePrimary = BrandPrimaryLight,
        surfaceDim = SurfaceContainer,
        surfaceBright = SurfaceElevated,
        scrim = TextPrimary.copy(alpha = 0.32f),
    )

// Premium Dark Color Scheme
private val DarkColorScheme =
    darkColorScheme(
        primary = BrandPrimaryLight,
        onPrimary = DarkBackgroundPrimary,
        primaryContainer = BrandPrimaryDark,
        onPrimaryContainer = BrandPrimaryLight,
        secondary = BrandSecondaryLight,
        onSecondary = DarkBackgroundPrimary,
        secondaryContainer = BrandSecondaryDark,
        onSecondaryContainer = BrandSecondaryLight,
        tertiary = BrandTertiaryLight,
        onTertiary = DarkBackgroundPrimary,
        tertiaryContainer = BrandTertiaryDark,
        onTertiaryContainer = BrandTertiaryLight,
        background = DarkBackgroundPrimary,
        onBackground = DarkTextPrimary,
        surface = DarkBackgroundSecondary,
        onSurface = DarkTextPrimary,
        surfaceVariant = DarkSurfaceVariant,
        onSurfaceVariant = DarkTextSecondary,
        error = ErrorRedLight,
        onError = DarkBackgroundPrimary,
        errorContainer = ErrorRedOnContainer,
        onErrorContainer = ErrorRedLight,
        outline = DarkBorder,
        outlineVariant = DarkOutline,
        surfaceContainer = DarkSurfaceVariant,
        surfaceContainerLow = DarkBackgroundSecondary,
        surfaceContainerLowest = DarkBackgroundPrimary,
        surfaceContainerHigh = DarkSurfaceVariant,
        surfaceContainerHighest = DarkBackgroundTertiary,
        inverseSurface = DarkTextPrimary,
        inverseOnSurface = DarkBackgroundPrimary,
        inversePrimary = BrandPrimaryDark,
    )

// Modern Shape System
private val AppShapes =
    Shapes(
        extraSmall = RoundedCornerShape(CornerRadius.xs),
        small = RoundedCornerShape(CornerRadius.sm),
        medium = RoundedCornerShape(CornerRadius.md),
        large = RoundedCornerShape(CornerRadius.lg),
        extraLarge = RoundedCornerShape(CornerRadius.xl),
    )

@Composable
fun AiProblemMakerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content,
    )
}
