package app.yskuem.aimondaimaker.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = BackgroundTertiary,
    primaryContainer = IconBackgroundBlue,
    onPrimaryContainer = PrimaryBlueDark,
    
    secondary = SecondaryPurple,
    onSecondary = BackgroundTertiary,
    secondaryContainer = IconBackgroundPurple,
    onSecondaryContainer = SecondaryPurpleDark,
    
    tertiary = SuccessGreen,
    onTertiary = BackgroundTertiary,
    tertiaryContainer = SuccessGreenLight,
    onTertiaryContainer = SuccessGreen,
    
    error = ErrorRed,
    onError = BackgroundTertiary,
    errorContainer = ErrorRedLight,
    onErrorContainer = ErrorRed,
    
    background = BackgroundPrimary,
    onBackground = TextPrimary,
    
    surface = BackgroundTertiary,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = TextSecondary,
    
    outline = BorderPrimary,
    outlineVariant = OutlineVariant,
    
    surfaceContainer = SurfaceContainer,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerLowest = BackgroundSecondary,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = BackgroundTertiary,
    
    inverseSurface = TextPrimary,
    inverseOnSurface = BackgroundTertiary,
    inversePrimary = PrimaryBlueLight,
    
    surfaceDim = SurfaceContainer,
    surfaceBright = BackgroundTertiary,
    
    scrim = TextPrimary.copy(alpha = 0.32f)
)

// Dark Color Scheme (for future implementation)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = DarkBackgroundPrimary,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,
    
    secondary = SecondaryPurpleLight,
    onSecondary = DarkBackgroundPrimary,
    secondaryContainer = SecondaryPurpleDark,
    onSecondaryContainer = SecondaryPurpleLight,
    
    background = DarkBackgroundPrimary,
    onBackground = DarkTextPrimary,
    
    surface = DarkBackgroundSecondary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = DarkTextSecondary,
    
    error = ErrorRed,
    onError = DarkBackgroundPrimary,
    
    outline = DarkTextSecondary,
    outlineVariant = DarkSurface
)

// Modern Shape System
private val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(CornerRadius.xs),
    small = RoundedCornerShape(CornerRadius.sm),
    medium = RoundedCornerShape(CornerRadius.md),
    large = RoundedCornerShape(CornerRadius.lg),
    extraLarge = RoundedCornerShape(CornerRadius.xl)
)

@Composable
fun AiProblemMakerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}