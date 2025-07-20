package app.yskuem.aimondaimaker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.theme.*

@Composable
fun PremiumCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: androidx.compose.ui.unit.Dp = Elevation.md,
    gradientColors: List<Color> = listOf(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.surface
    ),
    borderGradient: List<Color> = listOf(
        BorderAccent,
        BorderAccent.copy(alpha = 0.5f)
    ),
    content: @Composable ColumnScope.() -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Box(
        modifier = modifier
            .shadow(
                elevation = if (isPressed) elevation + 2.dp else elevation,
                shape = RoundedCornerShape(CornerRadius.lg),
                ambientColor = ShadowBrand,
                spotColor = ShadowMedium
            )
            .clip(RoundedCornerShape(CornerRadius.lg))
            .background(
                brush = Brush.verticalGradient(gradientColors)
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(borderGradient),
                shape = RoundedCornerShape(CornerRadius.lg)
            )
            .clickable { 
                isPressed = !isPressed
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(ComponentSpacing.cardPadding),
            content = content
        )
    }
}

@Composable
fun GlassMorphismCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = Elevation.sm,
                shape = RoundedCornerShape(GlassEffect.cornerRadius),
                ambientColor = GlassShadow,
                spotColor = GlassShadow
            )
            .clip(RoundedCornerShape(GlassEffect.cornerRadius))
            .background(GlassSurface)
            .border(
                width = GlassEffect.borderWidth,
                color = GlassBorder,
                shape = RoundedCornerShape(GlassEffect.cornerRadius)
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(ComponentSpacing.cardPadding),
            content = content
        )
    }
}

@Composable
fun GradientCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = GradientDawn,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = Elevation.lg,
                shape = RoundedCornerShape(CornerRadius.xl),
                ambientColor = ShadowBrand,
                spotColor = ShadowHeavy
            )
            .clip(RoundedCornerShape(CornerRadius.xl))
            .background(
                brush = Brush.linearGradient(gradientColors)
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(ComponentSpacing.cardPadding),
            content = content
        )
    }
}