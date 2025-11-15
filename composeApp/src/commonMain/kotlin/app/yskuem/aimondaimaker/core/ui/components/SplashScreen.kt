package app.yskuem.aimondaimaker.core.ui.components

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.ic_splash_back
import ai_problem_maker.composeapp.generated.resources.ic_splash_icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SplashScreen(
    onDismissed: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var showTitle by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // ① ロゴだけを少し先に出す
        delay(300L)

        // ② タイトル表示開始＆ハプティクス
        showTitle = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        // ③ 残り時間分待ってからスプラッシュ終了
        delay(1700L) // 合計で約 2000ms
        onDismissed()
    }

    FullScreenPhotoBackground(
        painter = painterResource(Res.drawable.ic_splash_back)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ロゴ
                Image(
                    painter = painterResource(Res.drawable.ic_splash_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .aspectRatio(1f),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                // アプリ名（フェードイン＋下からスライド＋少し拡大のアニメーション）
                AnimatedVisibility(
                    visible = showTitle,
                    enter = fadeIn(animationSpec = tween(durationMillis = 600)) +
                            slideInVertically(
                                initialOffsetY = { it / 3 },
                                animationSpec = tween(durationMillis = 600)
                            ) +
                            scaleIn(
                                initialScale = 0.9f,
                                animationSpec = tween(durationMillis = 600)
                            )
                ) {
                    Text(
                        text = "AI Note Scan",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FullScreenPhotoBackground(
    painter: Painter,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            // 必要に応じてステータスバー等のインセット調整をここで行う
            .windowInsetsPadding(WindowInsets(0, 0, 0, 0))
    ) {
        Image(
            painter = painter,
            contentDescription = "ロゴ",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        content()
    }
}
