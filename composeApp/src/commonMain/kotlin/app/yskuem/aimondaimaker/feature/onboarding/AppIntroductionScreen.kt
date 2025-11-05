package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class AppIntroductionScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        OnboardingWithVideo(
            onDone = {
                navigator?.replace(SelectProjectScreen())
            }
        )
    }
}