package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.core.util.getLocalVideoUrl

@Composable
actual fun getOnboardingVideoUrl(pageIndex: Int): String =
    when (pageIndex) {
        0 ->
            getLocalVideoUrl(
                iOSFilename = "onboarding_0",
            )
        1 ->
            getLocalVideoUrl(
                iOSFilename = "onboarding_1",
            )
        2 ->
            getLocalVideoUrl(
                iOSFilename = "onboarding_2",
            )
        3 ->
            getLocalVideoUrl(
                iOSFilename = "onboarding_3",
            )
        else -> throw IllegalArgumentException("Invalid page index")
    }
