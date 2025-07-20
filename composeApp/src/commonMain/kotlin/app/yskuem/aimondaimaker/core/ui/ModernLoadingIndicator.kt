package app.yskuem.aimondaimaker.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import app.yskuem.aimondaimaker.core.ui.theme.ComponentSpacing
import app.yskuem.aimondaimaker.core.ui.theme.Spacing
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ModernLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = ComponentSpacing.iconXLarge,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = Spacing.xs
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val animatedStrokeWidth by infiniteTransition.animateFloat(
        initialValue = strokeWidth.value * 0.5f,
        targetValue = strokeWidth.value * 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        val center = this.size.width / 2f
        val radius = center - animatedStrokeWidth * 2f
        
        rotate(rotation) {
            drawModernSpinner(
                center = center,
                radius = radius,
                color = color,
                strokeWidth = animatedStrokeWidth
            )
        }
    }
}

private fun DrawScope.drawModernSpinner(
    center: Float,
    radius: Float,
    color: Color,
    strokeWidth: Float
) {
    val numDots = 8
    val angleStep = 360f / numDots
    
    for (i in 0 until numDots) {
        val angle = angleStep * i
        val alpha = (1f - (i.toFloat() / numDots)) * 0.8f + 0.2f
        
        val radian = Math.toRadians(angle.toDouble())
        val x = center + cos(radian).toFloat() * radius
        val y = center + sin(radian).toFloat() * radius
        
        drawCircle(
            color = color.copy(alpha = alpha),
            radius = strokeWidth,
            center = androidx.compose.ui.geometry.Offset(x, y)
        )
    }
}