package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.components.SplashScreen
import app.yskuem.aimondaimaker.core.ui.theme.AiProblemMakerTheme
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    var shouldShowSplash by rememberSaveable {
        mutableStateOf(true)
    }

    AiProblemMakerTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            SystemPaddingContainer {
                GlobalToastContainer {
                    Navigator(UpdateCheckScreen()) { navigator ->
                        FadeTransition(
                            navigator = navigator,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = shouldShowSplash,
                enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 300)),
                modifier = Modifier.fillMaxSize()
            ) {
                SplashScreen(
                    onDismissed = {
                        shouldShowSplash = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SystemPaddingContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        content()
    }
}

