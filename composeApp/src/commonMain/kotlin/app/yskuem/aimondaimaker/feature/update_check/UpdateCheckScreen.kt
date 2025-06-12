package app.yskuem.aimondaimaker.feature.update_check

import ToastPosition
import androidx.compose.runtime.Composable
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import app.yskuem.aimondaimaker.feature.auth.ui.AuthScreen
import androidx.navigation.NavController
import org.koin.compose.koinInject
import kotlin.random.Random

@Composable
fun UpdateCheckScreen(
    navController: NavController,
) {
    val viewModel = koinInject<UpdateCheckScreenViewModel>()
    val state by viewModel.updateState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.checkUpdate()
    }
        when(val res = state) {
            is DataUiState.Error -> {
                navController.navigate("auth")
            }
            is DataUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            is DataUiState.Success -> {
                when(res.data) {
                    CheckUpdateStatus.UPDATED_NEEDED -> {
                        ForceUpdateScreen(
                            openStorePage = viewModel::openStorePage
                        )
                    }
                    CheckUpdateStatus.HAVE_LATEST_APP_VERSION -> {
                        LaunchedEffect(Unit) {
                            UpdateToastManager.show(
                                version = "",
                                duration = 15000L,
                                onUpdate = {
                                    println("アップデート開始")
                                    // アップデート処理
                                },
                                onDismiss = {
                                    println("トーストが閉じられました")
                                },
                                position = ToastPosition.TOP
                            )
                            navController.navigate("auth")
                        }
                    }
                    CheckUpdateStatus.NONE -> {
                        navController.navigate("auth")
                    }
                }
            }
        }
    }
}

@Composable
fun ForceUpdateScreen(
    openStorePage: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFF8E8FF), // 薄紫
                        Color(0xFFE8F4FD), // 薄青
                        Color(0xFFF0F8FF)  // アリスブルー
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

        // パーティクル効果
        FloatingParticles()

        // メインコンテンツ
        UpdateModal(
            openStorePage = openStorePage,
        )
    }
}

@Composable
fun AnimatedBackground() {
    val infiniteTransition = rememberInfiniteTransition()

    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val colors = listOf(
            Color(0xFFF8E8FF), // ラベンダーミスト
            Color(0xFFE8F4FD), // パウダーブルー
            Color(0xFFFFF0F5), // ラベンダーブラッシュ
            Color(0xFFE6F3FF)  // アイスブルー
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = colors,
                center = Offset(
                    size.width * (0.3f + animatedProgress * 0.4f),
                    size.height * (0.2f + animatedProgress * 0.6f)
                ),
                radius = size.width * (0.8f + animatedProgress * 0.4f)
            )
        )
    }
}

@Composable
fun FloatingShapes() {
    val shapes = remember {
        (0..5).map {
            FloatingShape(
                size = Random.nextInt(40, 80).dp,
                startX = Random.nextFloat(),
                animationDelay = Random.nextInt(0, 10000),
                color = listOf(
                    Color(0xFFFFB6C1), // ライトピンク
                    Color(0xFFB6E5D8), // ミントグリーン
                    Color(0xFFDDA0DD), // プラム
                    Color(0xFFADD8E6), // ライトブルー
                    Color(0xFFF0E68C)  // カーキ
                ).random()
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
    val animationDelay: Int,
    val color: Color
)

@Composable
fun AnimatedFloatingShape(shape: FloatingShape) {
    val infiniteTransition = rememberInfiniteTransition()

    val animatedY by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = -0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing, delayMillis = shape.animationDelay),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing, delayMillis = shape.animationDelay),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing, delayMillis = shape.animationDelay),
            repeatMode = RepeatMode.Reverse
        )
    )

    val alpha = when {
        animatedY > 0.9f -> (1f - animatedY) * 5f
        animatedY < 0.1f -> (animatedY + 0.2f) * 3f
        else -> 1f
    }.coerceIn(0f, 0.4f)

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
                .scale(animatedScale)
                .graphicsLayer {
                    rotationZ = animatedRotation
                    this.alpha = alpha
                }
                .blur(radius = 1.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            shape.color.copy(alpha = 0.6f),
                            shape.color.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun FloatingParticles() {
    val particles = remember {
        (0..8).map {
            Particle(
                startX = Random.nextFloat(),
                startY = Random.nextFloat(),
                animationDelay = Random.nextInt(0, 5000)
            )
        }
    }

    particles.forEach { particle ->
        AnimatedParticle(particle = particle)
    }
}

data class Particle(
    val startX: Float,
    val startY: Float,
    val animationDelay: Int
)

@Composable
fun AnimatedParticle(particle: Particle) {
    val infiniteTransition = rememberInfiniteTransition()

    val animatedY by infiniteTransition.animateFloat(
        initialValue = particle.startY,
        targetValue = particle.startY - 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing, delayMillis = particle.animationDelay),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing, delayMillis = particle.animationDelay),
            repeatMode = RepeatMode.Reverse
        )
    )

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .offset(
                    x = maxWidth * particle.startX,
                    y = maxHeight * animatedY
                )
                .background(
                    color = Color.White.copy(alpha = animatedAlpha * 0.7f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun UpdateModal(
    openStorePage: () -> Unit,
) {
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
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.95f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
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
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B46C1), // パープル
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // サブタイトル
                Text(
                    text = "アプリケーションを継続してご利用いただくため、最新バージョンへのアップデートをお願いします。",
                    fontSize = 15.sp,
                    color = Color(0xFF64748B), // スレートグレー
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // アップデートボタン
                UpdateButton(
                    openStorePage = openStorePage
                )
            }
        }
    }
}

@Composable
fun PulsingUpdateIcon() {
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        // グロー効果
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFE879F9).copy(alpha = glowAlpha * 0.3f),
                            Color.Transparent
                        ),
                        radius = 50.dp.value
                    ),
                    shape = CircleShape
                )
        )

        // メインアイコン
        Box(
            modifier = Modifier
                .size(75.dp)
                .scale(scale)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF8BBD9), // ピンク
                            Color(0xFFDDD6FE), // ラベンダー
                            Color(0xFFBFDBFE)  // ブルー
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Update",
                tint = Color(0xFF6B46C1),
                modifier = Modifier.size(35.dp)
            )
        }
    }
}

@Composable
fun UpdateButton(
    openStorePage: () -> Unit,
) {
    val buttonScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    Button(
        onClick = openStorePage,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(buttonScale),
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 2.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF8BBD9), // ソフトピンク
                            Color(0xFFDDD6FE), // ソフトラベンダー
                            Color(0xFFBFDBFE)  // ソフトブルー
                        )
                    ),
                    shape = RoundedCornerShape(26.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "今すぐアップデート",
                color = Color(0xFF6B46C1),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}