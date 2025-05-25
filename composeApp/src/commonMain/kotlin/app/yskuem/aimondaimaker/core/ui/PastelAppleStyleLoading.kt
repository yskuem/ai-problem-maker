import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.loading_explanation
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@Composable
fun PastelAppleStyleLoading(
    loadingTitle: String
) {
    var loadingText by remember { mutableStateOf(loadingTitle) }
    var isVisible by remember { mutableStateOf(false) }

    // テキストアニメーション
    LaunchedEffect(key1 = true) {
        val textStates = listOf(loadingTitle, "${loadingTitle}.", "${loadingTitle}..", "${loadingTitle}...")
        var index = 0

        // 初期フェードイン
        isVisible = true

        while (true) {
            delay(500)
            loadingText = textStates[index]
            index = (index + 1) % textStates.size
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF5F3FF), // purple-50
                        Color(0xFFEFF6FF), // blue-50
                        Color(0xFFFDF2F8)  // pink-50
                    )
                )
            )
    ) {
        // 背景のアクセント要素
        Box(
            modifier = Modifier
                .size(64.dp)
                .offset((-32).dp, (-32).dp)
                .blur(64.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x33FBCFE8), // pink-200 with alpha
                            Color(0x33DDD6FE)  // purple-200 with alpha
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.BottomEnd)
                .offset(32.dp, 32.dp)
                .blur(64.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x33BFDBFE), // blue-200 with alpha
                            Color(0x33A5F3FC)  // teal-200 with alpha
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.TopEnd)
                .offset((-48).dp, 120.dp)
                .blur(64.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x33FDE68A), // yellow-200 with alpha
                            Color(0x33FDBA74)  // orange-200 with alpha
                        )
                    ),
                    shape = CircleShape
                )
        )

        // メインのアニメーションコンテナ
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // アニメーションコンテナ
                Box(
                    modifier = Modifier.size(264.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // 外側の回転するリング
                    OuterRing()

                    // 光沢リング
                    ShineRing()

                    // メインのローダーリング
                    MainLoaderRing()

                    // 中央の脈動する光
                    PulsatingLight()

                    // 浮遊する光の粒子
                    FloatingParticles()
                }

                // テキスト部分
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = loadingText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151), // gray-700
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                FadingText(
                    text = stringResource(Res.string.loading_explanation),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF6B7280), // gray-500
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun AnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "visibility"
    )

    Box(
        modifier = modifier.alpha(alpha)
    ) {
        content()
    }
}

@Composable
fun OuterRing() {
    val infiniteTransition = rememberInfiniteTransition(label = "outer_ring")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing)
        ),
        label = "outer_rotation"
    )

    Box(
        modifier = Modifier
            .size(224.dp)
            .clip(CircleShape)
            .alpha(0.2f)
            .drawBehind {
                rotate(rotation) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFFA78BFA), // purple-400
                                Color(0xFF60A5FA), // blue-400
                                Color(0xFFF472B6), // pink-400
                                Color(0xFFA78BFA)  // purple-400 (repeat for smooth gradient)
                            )
                        ),
                        radius = size.minDimension / 2,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
    ) {
        // 内部の白い円
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(Color.White)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ShineRing() {
    // 無限回転アニメーション
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15_000, easing = LinearEasing)
        )
    )

    Canvas(modifier = Modifier.size(192.dp)) {
        rotate(rotation) {
            // スイープグラデーションを生成
            val brush = Brush.sweepGradient(
                listOf(
                    Color(0xCCBFDBFE),
                    Color(0xCCC4B5FD),
                    Color(0xCCFCA5A5),
                    Color(0xCC99F6E4),
                    Color(0xCCBFDBFE)
                )
            )
            val strokeWidth = 0.7.dp.toPx()
            // ダッシュパターン [実線:1px, 空白:8px]
            val dashArray = floatArrayOf(1f, 8f)

            // 36本の短いアークを繰り返し描画
            repeat(36) { i ->
                val startAngle = i * (360f / 36f)
                drawArc(
                    brush = brush,
                    startAngle = startAngle,
                    sweepAngle = 1f,
                    useCenter = false,
                    topLeft = Offset(
                        x = (size.width - size.minDimension) / 2,
                        y = (size.height - size.minDimension) / 2
                    ),
                    size = Size(size.minDimension, size.minDimension),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(dashArray, 0f)
                    )
                )
            }
        }
    }
}

@Composable
fun MainLoaderRing() {
    val infiniteTransition = rememberInfiniteTransition(label = "main_loader")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        ),
        label = "main_rotation"
    )

    Canvas(modifier = Modifier.size(160.dp)) {
        rotate(rotation) {
            val colors = listOf(
                Color(0xFFA78BFA), // purple-400
                Color(0xFF60A5FA), // blue-400
                Color(0xFFF472B6), // pink-400
            )

            drawCircle(
                brush = Brush.sweepGradient(colors),
                radius = size.minDimension / 2,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 60f), 0f)
                )
            )
        }
    }
}

@Composable
fun PulsatingLight() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulsating_light")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "light_scale"
    )

    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, delayMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "light_scale2"
    )

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    // 外側の大きな光
    Box(
        modifier = Modifier
            .size(96.dp * scale)
            .alpha(0.7f)
            .blur(16.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFDFE9F3), // blue-100
                        Color(0xFFEDE9FE)  // purple-100
                    )
                ),
                shape = CircleShape
            )
    )

    // 中間サイズの光
    Box(
        modifier = Modifier
            .size(64.dp * scale2)
            .alpha(0.7f)
            .blur(8.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFBFDBFE), // blue-200
                        Color(0xFFF5D0FE)  // pink-200
                    )
                ),
                shape = CircleShape
            )
    )

    // 中央の白い光
    Box(
        modifier = Modifier
            .size(40.dp * glowScale)
            .alpha(glowAlpha)
            .blur(4.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
    )
}

@Composable
fun FloatingParticles() {
    val particleCount = 24
    val particles = remember {
        List(particleCount) {
            ParticleData(
                initialX = Random.nextFloat() * 200f - 100f,
                initialY = Random.nextFloat() * 200f - 100f,
                color = when (it % 4) {
                    0 -> Color(0xB3A78BFA) // Purple with alpha
                    1 -> Color(0xB360A5FA) // Blue with alpha
                    2 -> Color(0xB3F472B6) // Pink with alpha
                    else -> Color(0xB399F6E4) // Teal with alpha
                },
                delay = Random.nextInt(5000),
                duration = 5000 + Random.nextInt(3000)
            )
        }
    }

    particles.forEach { particle ->
        FloatingParticle(particle)
    }
}

data class ParticleData(
    val initialX: Float,
    val initialY: Float,
    val color: Color,
    val delay: Int,
    val duration: Int
)

@Composable
fun FloatingParticle(data: ParticleData) {
    val infiniteTransition = rememberInfiniteTransition(label = "particle")

    val xOffset by infiniteTransition.animateFloat(
        initialValue = data.initialX,
        targetValue = data.initialX + 20f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = data.duration
                data.initialX at 0 using LinearEasing
                data.initialX + 10f at data.duration / 4 using LinearEasing
                data.initialX - 5f at data.duration / 2 using LinearEasing
                data.initialX - 10f at 3 * data.duration / 4 using LinearEasing
                data.initialX at data.duration using LinearEasing
            },
            initialStartOffset = StartOffset(offsetMillis = data.delay)
        ),
        label = "x_offset"
    )

    val yOffset by infiniteTransition.animateFloat(
        initialValue = data.initialY,
        targetValue = data.initialY - 20f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = data.duration
                data.initialY at 0 using LinearEasing
                data.initialY - 10f at data.duration / 4 using LinearEasing
                data.initialY - 20f at data.duration / 2 using LinearEasing
                data.initialY - 10f at 3 * data.duration / 4 using LinearEasing
                data.initialY at data.duration using LinearEasing
            },
            initialStartOffset = StartOffset(offsetMillis = data.delay)
        ),
        label = "y_offset"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = data.duration
                0f at 0 using LinearEasing
                0.8f at data.duration / 4 using LinearEasing
                0.6f at data.duration / 2 using LinearEasing
                0.8f at 3 * data.duration / 4 using LinearEasing
                0f at data.duration using LinearEasing
            },
            initialStartOffset = StartOffset(offsetMillis = data.delay)
        ),
        label = "alpha"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = data.color,
            radius = 2.dp.toPx(),
            center = Offset(size.width / 2 + xOffset, size.height / 2 + yOffset),
            alpha = alpha
        )
    }
}

@Composable
fun FadingText(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight,
    color: Color,
    letterSpacing: androidx.compose.ui.unit.TextUnit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "text_fade")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_alpha"
    )

    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = color.copy(alpha = alpha),
        letterSpacing = letterSpacing,
        textAlign = TextAlign.Center
    )
}
