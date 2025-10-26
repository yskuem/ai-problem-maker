package app.yskuem.aimondaimaker.feature.quiz.ui

import androidx.compose.animation.core.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.*

@Composable
fun PdfGenerateLoading() {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f

    // Colors - プライマリーカラーを基調
    val base = MaterialTheme.colorScheme.primary
    val light = lerp(base, Color.White, if (isDark) 0.35f else 0.55f)
    val veryLight = lerp(base, Color.White, if (isDark) 0.80f else 0.92f)

    // 背景グラデーション - プライマリーカラーベース
    val bgGradient = if (isDark) {
        listOf(
            base.copy(alpha = 0.05f).compositeOver(Color(0xFF0B0F14)),
            base.copy(alpha = 0.08f).compositeOver(Color(0xFF101622)),
            base.copy(alpha = 0.12f).compositeOver(Color(0xFF17142A))
        )
    } else {
        listOf(
            lerp(base, Color.White, 0.95f),
            lerp(base, Color.White, 0.92f),
            lerp(base, Color.White, 0.88f)
        )
    }

    val cardBg = if (isDark) Color.White.copy(alpha = 0.06f) else Color.White.copy(alpha = 0.9f)
    val lineColor = if (isDark) light else base.copy(alpha = 0.6f)
    val progressTrack = if (isDark) Color.White.copy(alpha = 0.12f) else veryLight
    val aura = base.copy(alpha = if (isDark) 0.18f else 0.10f)
    val titleColor = if (isDark) light else base
    val messageColor = if (isDark) Color.White.copy(alpha = 0.7f) else base.copy(alpha = 0.7f)

    val dotColors = listOf(
        base,
        lerp(base, Color.White, 0.25f),
        lerp(base, Color.White, 0.45f)
    )

    // Animations
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float"
    )

    val pencilProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pencil"
    )

    val dotsProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dots"
    )

    val progressValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    // Messages
    val messages = listOf(
        "問題のPDFを生成中...",
        "解答のPDFを生成中..."
    )

    var currentMessageIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentMessageIndex = (currentMessageIndex + 1) % messages.size
        }
    }

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(bgGradient, start = Offset.Zero, end = Offset.Infinite))
        ) {
            // Floating circles
            repeat(4) { index ->
                FloatingCircle(index, aura)
            }

            // Main content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .offset(y = (-floatOffset).dp)
                        .then(
                            if (isDark) {
                                Modifier.border(1.dp, base.copy(alpha = 0.20f), RoundedCornerShape(30.dp))
                            } else {
                                Modifier.border(1.dp, base.copy(alpha = 0.10f), RoundedCornerShape(30.dp))
                            }
                        )
                        .background(cardBg, RoundedCornerShape(30.dp))
                        .padding(horizontal = 40.dp, vertical = 60.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Pencil + paper animation
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            PaperLines(lineColor, pencilProgress)
                            Pencil(base, light, pencilProgress)
                            Particles(base, pencilProgress)
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        LoadingDots(dotColors, dotsProgress)

                        Spacer(modifier = Modifier.height(25.dp))

                        Text(
                            text = "PDF生成中",
                            fontSize = 24.sp,
                            color = titleColor,
                            fontWeight = FontWeight.Light,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        AnimatedContent(
                            targetState = currentMessageIndex,
                            transitionSpec = {
                                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                            },
                            label = "message"
                        ) { index ->
                            Text(
                                text = messages[index],
                                fontSize = 14.sp,
                                color = messageColor,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        ProgressBar(base, lerp(base, Color.White, 0.25f), progressTrack, progressValue)
                    }
                }
            }
        }
    }
}

@Composable
fun FloatingCircle(index: Int, auraColor: Color) {
    val sizes = listOf(100.dp, 150.dp, 80.dp, 120.dp)
    val alignments = listOf(
        BiasAlignment(-0.8f, -0.8f),
        BiasAlignment(0.8f, 0.8f),
        BiasAlignment(-0.8f, 0.6f),
        BiasAlignment(0.8f, -0.6f)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "circle$index")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween((20 + index * 5) * 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation$index"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignments[index]
    ) {
        Box(
            modifier = Modifier
                .size(sizes[index])
                .rotate(rotation)
                .background(
                    Brush.radialGradient(listOf(auraColor, Color.Transparent)),
                    CircleShape
                )
        )
    }
}

@Composable
fun Pencil(base: Color, light: Color, progress: Float) {
    val scale = progress
    val rotation = sin(progress * PI.toFloat() * 2) * 0.3f

    Box(
        modifier = Modifier
            .graphicsLayer {
                rotationZ = rotation * 180f / PI.toFloat()
                scaleX = scale
                transformOrigin = TransformOrigin(0f, 0.5f)
            }
            .width(60.dp)
            .height(8.dp)
            .background(
                Brush.linearGradient(
                    listOf(base, lerp(base, Color.White, 0.18f), light)
                ),
                RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.CenterEnd
    ) {
        Canvas(modifier = Modifier.size(8.dp)) {
            val path = Path().apply {
                moveTo(0f, size.height / 2)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color = base)
        }
    }
}

@Composable
fun PaperLines(lineColor: Color, progress: Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        repeat(4) { index ->
            val delay = index * 0.2f
            val opacity = abs(((progress + delay) % 1) * 2 - 1)

            Box(
                modifier = Modifier
                    .width((80 - (index % 2) * 10).dp)
                    .height(2.dp)
                    .padding(vertical = 4.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color.Transparent,
                                lineColor.copy(alpha = opacity),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}

@Composable
fun LoadingDots(colors: List<Color>, progress: Float) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(3) { index ->
            val delay = index * 0.2f
            val value = (progress + delay) % 1
            val scale = 1f + (sin(value * PI.toFloat() * 2) * 0.3f)

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .scale(scale)
                    .background(colors[index], CircleShape)
            )
        }
    }
}

@Composable
fun ProgressBar(start: Color, end: Color, track: Color, progress: Float) {
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(4.dp)
            .background(track, RoundedCornerShape(2.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(
                    Brush.linearGradient(listOf(start, end)),
                    RoundedCornerShape(2.dp)
                )
        )
    }
}

@Composable
fun Particles(color: Color, progress: Float) {
    Canvas(modifier = Modifier.size(120.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val random = kotlin.random.Random(42)

        repeat(5) {
            val angle = random.nextDouble() * PI * 2
            val distance = progress * 30 * (1 + random.nextDouble()).toFloat()
            val particleSize = 2 * (1 - progress)

            if (particleSize > 0) {
                drawCircle(
                    color = color.copy(alpha = 0.6f * (1 - progress)),
                    radius = particleSize,
                    center = center + Offset(
                        (cos(angle) * distance).toFloat(),
                        (sin(angle) * distance).toFloat()
                    )
                )
            }
        }
    }
}

fun lerp(start: Color, end: Color, fraction: Float): Color {
    return androidx.compose.ui.graphics.lerp(start, end, fraction.coerceIn(0f, 1f))
}