package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key.Companion.R
import app.yskuem.aimondaimaker.core.util.getLocalVideoUrl
import cafe.adriel.voyager.core.screen.Screen
import io.github.yskuem.onboarding.api.IntroductionScreen

class AppIntroductionScreen: Screen {
    @Composable
    override fun Content() {
        OnboardingWithVideo(
            onDone = {}
        )
    }
}