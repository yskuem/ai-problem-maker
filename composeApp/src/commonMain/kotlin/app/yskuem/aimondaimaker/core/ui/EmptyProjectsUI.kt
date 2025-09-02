package app.yskuem.aimondaimaker.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import app.yskuem.aimondaimaker.core.ui.theme.ComponentSpacing
import app.yskuem.aimondaimaker.core.ui.theme.Spacing

@Composable
fun EmptyProjectsUI(
    message: String,
    iconVector: ImageVector,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(Spacing.xxxl),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Modern icon display with improved styling
            Box(
                modifier =
                    Modifier
                        .size(ComponentSpacing.iconXXLarge * 2)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.extraLarge,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    modifier = Modifier.size(ComponentSpacing.iconXXLarge),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(Spacing.xxl))

            // Improved message text with theme colors
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
