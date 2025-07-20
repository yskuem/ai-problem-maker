package app.yskuem.aimondaimaker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.theme.*

@Composable
fun PremiumSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .shadow(
                    elevation = Elevation.sm,
                    shape = RoundedCornerShape(CornerRadius.xl),
                    ambientColor = ShadowLight,
                    spotColor = ShadowMedium,
                ).clip(RoundedCornerShape(CornerRadius.xl))
                .background(
                    brush =
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.surface,
                                MaterialTheme.colorScheme.surfaceContainer,
                            ),
                        ),
                ).border(
                    width = 1.dp,
                    color = BorderAccent,
                    shape = RoundedCornerShape(CornerRadius.xl),
                ),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(CornerRadius.xl),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
            textStyle =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions =
                KeyboardActions(
                    onSearch = { onSearch() },
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
        )
    }
}

@Composable
fun GlassSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    onSearch: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .shadow(
                    elevation = Elevation.xs,
                    shape = RoundedCornerShape(CornerRadius.xxl),
                    ambientColor = GlassShadow,
                    spotColor = GlassShadow,
                ).clip(RoundedCornerShape(CornerRadius.xxl))
                .background(GlassSurface)
                .border(
                    width = 1.dp,
                    color = GlassBorder,
                    shape = RoundedCornerShape(CornerRadius.xxl),
                ),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(CornerRadius.xxl),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                ),
            textStyle =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions =
                KeyboardActions(
                    onSearch = { onSearch() },
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
        )
    }
}
