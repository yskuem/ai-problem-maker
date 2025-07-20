package app.yskuem.aimondaimaker.core.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Design System Spacing Scale (8dp base unit)
object Spacing {
    // Base unit for consistent spacing
    val base = 8.dp
    
    // Spacing Scale
    val none = 0.dp
    val xs = 4.dp      // 0.5 * base
    val sm = 8.dp      // 1 * base
    val md = 12.dp     // 1.5 * base
    val lg = 16.dp     // 2 * base
    val xl = 20.dp     // 2.5 * base
    val xxl = 24.dp    // 3 * base
    val xxxl = 32.dp   // 4 * base
    val huge = 40.dp   // 5 * base
    val mega = 48.dp   // 6 * base
    val ultra = 64.dp  // 8 * base
}

// Component-specific spacing
object ComponentSpacing {
    // Card padding
    val cardPadding = Spacing.lg
    val cardInnerPadding = Spacing.md
    val cardSpacing = Spacing.md
    
    // Button spacing
    val buttonPadding = Spacing.lg
    val buttonIconSpacing = Spacing.sm
    val buttonVerticalSpacing = Spacing.md
    
    // Screen padding
    val screenPadding = Spacing.lg
    val screenTopPadding = Spacing.xxxl
    val sectionSpacing = Spacing.xxl
    
    // List item spacing
    val listItemPadding = Spacing.lg
    val listItemSpacing = Spacing.sm
    val listIconSpacing = Spacing.md
    
    // Form spacing
    val fieldSpacing = Spacing.lg
    val formSectionSpacing = Spacing.xxl
    
    // Icon sizes
    val iconSmall = 16.dp
    val iconMedium = 24.dp
    val iconLarge = 32.dp
    val iconXLarge = 40.dp
    val iconXXLarge = 48.dp
}

// Border radius values
object CornerRadius {
    val none = 0.dp
    val xs = 4.dp
    val sm = 6.dp
    val md = 8.dp
    val lg = 12.dp
    val xl = 16.dp
    val xxl = 20.dp
    val round = 50.dp
    val full = 9999.dp  // For circular shapes
}

// Elevation values for modern depth
object Elevation {
    val none = 0.dp
    val xs = 1.dp
    val sm = 2.dp
    val md = 4.dp
    val lg = 6.dp
    val xl = 8.dp
    val xxl = 12.dp
    val huge = 16.dp
}