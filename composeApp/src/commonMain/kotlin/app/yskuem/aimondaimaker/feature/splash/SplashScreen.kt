package app.yskuem.aimondaimaker.feature.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.theme.BrandPrimary
import app.yskuem.aimondaimaker.core.ui.theme.BrandPrimaryDark
import app.yskuem.aimondaimaker.core.ui.theme.BrandPrimaryLight
import app.yskuem.aimondaimaker.core.ui.theme.BrandSecondary
import app.yskuem.aimondaimaker.core.ui.theme.BrandTertiaryLight
import app.yskuem.aimondaimaker.core.ui.theme.ShadowBrand

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BrandPrimaryDark,
                            BrandPrimary,
                            BrandSecondary,
                        ),
                    ),
                ),
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = glow * 0.8f),
                                Color.Transparent,
                            ),
                        )
                    )
                    .alpha(0.35f),
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = pulse
                            scaleY = pulse
                        }
                        .shadow(
                            elevation = 32.dp,
                            shape = RoundedCornerShape(48.dp),
                            ambientColor = ShadowBrand,
                            spotColor = ShadowBrand,
                        )
                        .clip(RoundedCornerShape(48.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    BrandPrimaryLight.copy(alpha = 0.95f),
                                    BrandPrimary,
                                    BrandTertiaryLight.copy(alpha = 0.9f),
                                ),
                            ),
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color.White.copy(alpha = 0.15f),
                                    ),
                                ),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(64.dp),
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = BrandPrimaryDark,
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AI Problem Maker",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "Create inspired quizzes in seconds",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.85f),
                    )
                }
            }
        }
    }
}
