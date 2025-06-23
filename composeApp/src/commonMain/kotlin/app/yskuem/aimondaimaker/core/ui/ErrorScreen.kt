package app.yskuem.aimondaimaker.core.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.back_to_pre_screen
import ai_problem_maker.composeapp.generated.resources.error_screen_back_action
import ai_problem_maker.composeapp.generated.resources.error_screen_reload_action
import ai_problem_maker.composeapp.generated.resources.load_again
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
    val lightPurple = Color(0xFFE8D5FF)
    val mediumPurple = Color(0xFFD4BFFF)
    val deepPurple = Color(0xFFB19AE8)
    val darkPurple = Color(0xFF8B7BB8)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    lightPurple,
                                    mediumPurple,
                                ),
                        ),
                ),
    ) {
        Card(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(32.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                    ),
            shape = RoundedCornerShape(24.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f),
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // エラーアイコン
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = deepPurple,
                )

                // エラーメッセージ
                Text(
                    text =
                        when (type) {
                            ErrorScreenType.RELOAD,
                            -> stringResource(Res.string.error_screen_reload_action)
                            ErrorScreenType.BACK,
                            -> stringResource(Res.string.error_screen_back_action)
                        },
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = darkPurple,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                )

                // アクションボタン
                Button(
                    onClick = {
                        when (type) {
                            ErrorScreenType.RELOAD -> onRefresh()
                            ErrorScreenType.BACK -> {
                                navigator?.pop()
                            }
                        }
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = deepPurple,
                            contentColor = Color.White,
                        ),
                    elevation =
                        ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp,
                        ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector =
                                when (type) {
                                    ErrorScreenType.RELOAD -> Icons.Default.Refresh
                                    ErrorScreenType.BACK -> Icons.Default.ArrowBack
                                },
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text =
                                when (type) {
                                    ErrorScreenType.RELOAD ->
                                        stringResource(Res.string.load_again)
                                    ErrorScreenType.BACK ->
                                        stringResource(Res.string.back_to_pre_screen)
                                },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
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
        type = ErrorScreenType.BACK,
        onRefresh = {},
    )
}
