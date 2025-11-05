package app.yskuem.aimondaimaker

import GlobalToastContainer
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import app.yskuem.aimondaimaker.core.ui.theme.AiProblemMakerTheme
import app.yskuem.aimondaimaker.feature.onboarding.AppIntroductionScreen
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
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
}
