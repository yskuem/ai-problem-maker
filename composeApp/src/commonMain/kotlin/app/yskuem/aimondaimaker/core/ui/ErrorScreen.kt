package app.yskuem.aimondaimaker.core.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.back_to_pre_screen
import ai_problem_maker.composeapp.generated.resources.error_screen_back_action
import ai_problem_maker.composeapp.generated.resources.error_screen_reload_action
import ai_problem_maker.composeapp.generated.resources.load_again
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorScreen(
    type: ErrorScreenType,
    onRefresh: () -> Unit = {},
) {
    val navigator = LocalNavigator.current

    // パステルパープルのカラーパレット
    val lightPurple = Color(0xFFE6E1FF)
    val mediumPurple = Color(0xFFD1C9FF)
    val darkPurple = Color(0xFFB19CFF)
    val accentPurple = Color(0xFF9B85FF)
    val softPurple = Color(0xFFE9E4FF)

    // エラータイプに応じたアイコンとカラーの設定
    val (icon, iconColor, buttonColor) = when (type) {
        ErrorScreenType.RELOAD -> Triple(
            Icons.Default.Refresh,
            Color(0xFF7C6FFF),
            accentPurple
        )
        ErrorScreenType.BACK -> Triple(
            Icons.Default.ArrowBack,
            Color(0xFF9B85FF),
            darkPurple
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lightPurple,
                        softPurple.copy(alpha = 0.8f),
                        mediumPurple.copy(alpha = 0.6f)
                    )
                )
            ),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // アイコンの背景とアニメーション効果
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                iconColor.copy(alpha = 0.15f),
                                iconColor.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 内側の円
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    iconColor.copy(alpha = 0.2f),
                                    iconColor.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = iconColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // エラーメッセージ
            Text(
                text = when (type) {
                    ErrorScreenType.RELOAD ->
                        stringResource(Res.string.error_screen_reload_action)
                    ErrorScreenType.BACK ->
                        stringResource(Res.string.error_screen_back_action)
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B4EFF),
                textAlign = TextAlign.Center,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // メインアクションボタン
            Button(
                onClick = {
                    when (type) {
                        ErrorScreenType.RELOAD -> onRefresh()
                        ErrorScreenType.BACK -> navigator?.pop()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 8.dp),
                    tint = Color.White
                )
                Text(
                    text = when (type) {
                        ErrorScreenType.RELOAD ->
                            stringResource(Res.string.load_again)
                        ErrorScreenType.BACK ->
                            stringResource(Res.string.back_to_pre_screen)
                    },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 装飾的な要素
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == 1) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                iconColor.copy(
                                    alpha = if (index == 1) 0.6f else 0.3f
                                )
                            )
                    )
                }
            }
        }

        // 背景の装飾的なサークル
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset((-50).dp, (-50).dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            lightPurple.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.BottomEnd)
                .offset(50.dp, 50.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            mediumPurple.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}

// 更新するか前の画面に戻るかどうか
enum class ErrorScreenType {
    RELOAD,
    BACK,
}


@Preview
@Composable
fun ErrorScreenPreview() {
    ErrorScreen(
        type = ErrorScreenType.RELOAD,
        onRefresh = {}
    )
}