package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import app.yskuem.aimondaimaker.core.ui.theme.AiProblemMakerTheme
import app.yskuem.aimondaimaker.feature.splash.SplashScreen
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
@Preview
fun App() {
    AiProblemMakerTheme {
        var showSplash by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            delay(1800)
            showSplash = false
        }

        Crossfade(
            targetState = showSplash,
            animationSpec = tween(durationMillis = 700),
        ) { isSplash ->
            if (isSplash) {
                SplashScreen()
            } else {
                GlobalToastContainer {
                    Navigator(UpdateCheckScreen()) { navigator ->
                        FadeTransition(
                            navigator = navigator,
                            animationSpec = spring(stiffness = Spring.StiffnessLow),
                        )
                    }
                }
            }
        }
    }
}
