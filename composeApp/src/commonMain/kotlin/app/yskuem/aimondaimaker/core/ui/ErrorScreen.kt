package app.yskuem.aimondaimaker.core.ui

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.back_to_pre_screen
import ai_problem_maker.composeapp.generated.resources.error_screen_back_action
import ai_problem_maker.composeapp.generated.resources.error_screen_reload_action
import ai_problem_maker.composeapp.generated.resources.load_again
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(
    type: ErrorScreenType,
    onRefresh: () -> Unit = {},
) {
    val navigator = LocalNavigator.current
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = when(type) {
                    ErrorScreenType.RELOAD
                        -> stringResource(Res.string.error_screen_reload_action)
                    ErrorScreenType.BACK
                        -> stringResource(Res.string.error_screen_back_action)
                },
                fontSize = 20.sp,
            )
            Button(
                onClick = {
                    when(type) {
                        ErrorScreenType.RELOAD -> onRefresh()
                        ErrorScreenType.BACK -> {
                            navigator?.pop()
                        }
                    }
                },
            ) {
                Text(
                    text = when(type) {
                        ErrorScreenType.RELOAD ->
                            stringResource(Res.string.load_again)
                        ErrorScreenType.BACK ->
                            stringResource(Res.string.back_to_pre_screen)
                    }
                )
            }
        }
    }
}

// 更新するか前の画面に戻るかどうか
enum class ErrorScreenType {
    RELOAD,
    BACK,
}
