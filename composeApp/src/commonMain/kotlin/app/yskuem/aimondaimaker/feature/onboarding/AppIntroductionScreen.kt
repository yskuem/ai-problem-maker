package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class AppIntroductionScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        OnboardingWithVideo(
            onDone = {
                navigator?.replace(WelcomeScreen())
            },
        )
    }
}
