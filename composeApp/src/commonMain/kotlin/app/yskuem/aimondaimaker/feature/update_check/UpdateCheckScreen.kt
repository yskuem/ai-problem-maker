package app.yskuem.aimondaimaker.feature.update_check

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class UpdateCheckScreen: Screen {
    @Composable
    override fun Content() {
        ForceUpdateScreen()
    }
}


@Composable
fun ForceUpdateScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        // 背景アニメーション
        AnimatedBackground()

        // 浮遊する図形
        FloatingShapes()

        // メインコンテンツ
        UpdateModal()
    }
}

@Composable
fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val colors = listOf(
            Color(0xFF667EEA),
            Color(0xFF764BA2),
            Color(0xFFF093FB),
            Color(0xFFF5576C)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width * animatedProgress, 0f),
                end = Offset(size.width * (1 - animatedProgress), size.height)
            )
        )
    }
}

@Composable
fun FloatingShapes() {
    val shapes = remember {
        (0..3).map {
            FloatingShape(
                size = Random.nextInt(60, 120).dp,
                startX = Random.nextFloat(),
                animationDelay = Random.nextInt(0, 15000)
            )
        }
    }

    shapes.forEach { shape ->
        AnimatedFloatingShape(shape = shape)
    }
}

data class FloatingShape(
    val size: androidx.compose.ui.unit.Dp,
    val startX: Float,
    val animationDelay: Int
)

@Composable
fun AnimatedFloatingShape(shape: FloatingShape) {
    val infiniteTransition = rememberInfiniteTransition()

    val animatedY by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = -0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing, delayMillis = shape.animationDelay),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing, delayMillis = shape.animationDelay),
            repeatMode = RepeatMode.Restart
        )
    )

    val alpha = when {
        animatedY > 0.9f -> (1f - animatedY) * 10f
        animatedY < 0.1f -> animatedY * 10f
        else -> 1f
    }.coerceIn(0f, 0.3f)

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(shape.size)
                .offset(
                    x = maxWidth * shape.startX,
                    y = maxHeight * animatedY
                )
                .graphicsLayer {
                    rotationZ = animatedRotation
                    this.alpha = alpha
                }
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun UpdateModal() {
    val modalScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .widthIn(max = 480.dp)
                .scale(modalScale),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 25.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(40.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // アップデートアイコン
                PulsingUpdateIcon()

                Spacer(modifier = Modifier.height(24.dp))

                // タイトル
                Text(
                    text = "アップデートが必要です",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3748),
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // サブタイトル
                Text(
                    text = "アプリケーションを継続してご利用いただくため、最新バージョンへのアップデートをお願いします。",
                    fontSize = 16.sp,
                    color = Color(0xFF4A5568),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // アップデートボタン
                UpdateButton()
            }
        }
    }
}

@Composable
fun PulsingUpdateIcon() {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(scale)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                ),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Update",
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun UpdateButton() {
    Button(
        onClick = { /* ここに実際のアップデート処理を実装 */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF667EEA),
                            Color(0xFF764BA2)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "今すぐアップデート",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}