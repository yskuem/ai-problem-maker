package app.yskuem.aimondaimaker.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.theme.*

@Composable
fun CreateNewButton(
    modifier: Modifier = Modifier,
    buttonText: String,
    onClick: () -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(60.dp)
                .shadow(
                    elevation = if (isPressed) Elevation.lg else Elevation.xl,
                    shape = RoundedCornerShape(CornerRadius.xl),
                    ambientColor = ShadowBrand,
                    spotColor = ShadowHeavy,
                ).clip(RoundedCornerShape(CornerRadius.xl))
                .background(
                    brush =
                        Brush.linearGradient(
                            listOf(
                                BrandPrimary,
                                BrandSecondary,
                                BrandTertiary.copy(alpha = 0.8f),
                            ),
                        ),
                ).border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(CornerRadius.xl),
                ).clickable {
                    isPressed = !isPressed
                    onClick()
                },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(Spacing.sm))
            Text(
                text = buttonText,
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    ),
            )
        }
    }
}
