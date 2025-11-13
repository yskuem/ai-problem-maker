package app.yskuem.aimondaimaker

import GlobalToastContainer
import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.ic_splash_back
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.components.FullScreenPhotoBackground
import app.yskuem.aimondaimaker.core.ui.components.SplashLottie
import app.yskuem.aimondaimaker.core.ui.components.SplashScreen
import app.yskuem.aimondaimaker.core.ui.theme.AiProblemMakerTheme
import app.yskuem.aimondaimaker.feature.onboarding.AppIntroductionScreen
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val shouldShowSplash = rememberSaveable {
        mutableStateOf(true)
    }

    AiProblemMakerTheme {
        GlobalToastContainer {
            Navigator(UpdateCheckScreen()) { navigator ->
                FadeTransition(
                    navigator = navigator,
                    animationSpec = spring(stiffness = Spring.StiffnessLow),
                )
            }
        }
    }
    if(shouldShowSplash.value) {
        SplashScreen(
            onDismissed = {
                shouldShowSplash.value = false
            }
        )
    }
}
