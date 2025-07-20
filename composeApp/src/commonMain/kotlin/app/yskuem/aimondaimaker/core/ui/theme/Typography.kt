package app.yskuem.aimondaimaker.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Premium Typography Scale - Enhanced for Visual Impact
val AppTypography =
    Typography(
        // Display Styles - Hero headlines with dramatic impact
        displayLarge =
            TextStyle(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
            ),
        displayMedium =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.25).sp,
            ),
        displaySmall =
            TextStyle(
                fontSize = 28.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.1).sp,
            ),
        // Headline Styles - Prominent section headers
        headlineLarge =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.1).sp,
            ),
        headlineMedium =
            TextStyle(
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
            ),
        headlineSmall =
            TextStyle(
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
            ),
        // Title Styles - Enhanced readability and hierarchy
        titleLarge =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.05.sp,
            ),
        titleSmall =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.1.sp,
            ),
        // Body Styles - Optimized for readability
        bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.1.sp,
            ),
        bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.15.sp,
            ),
        bodySmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.2.sp,
            ),
        // Label Styles - Clear and accessible
        labelLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.1.sp,
            ),
        labelMedium =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.4.sp,
            ),
        labelSmall =
            TextStyle(
                fontSize = 11.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.4.sp,
            ),
    )

// Custom Typography Extensions for Special Cases
val Typography.displayExtraLarge: TextStyle
    get() =
        TextStyle(
            fontSize = 42.sp,
            lineHeight = 50.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.75).sp,
        )

val Typography.headlineExtraLarge: TextStyle
    get() =
        TextStyle(
            fontSize = 28.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.2).sp,
        )

val Typography.titleExtraLarge: TextStyle
    get() =
        TextStyle(
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.sp,
        )

val Typography.captionEmphasized: TextStyle
    get() =
        TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
        )
