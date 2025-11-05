package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.R
import app.yskuem.aimondaimaker.core.util.getLocalVideoUrl
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreen
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.github.yskuem.onboarding.api.IntroductionScreen

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