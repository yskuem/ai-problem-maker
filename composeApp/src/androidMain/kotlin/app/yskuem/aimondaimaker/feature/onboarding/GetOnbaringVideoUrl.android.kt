package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.R
import app.yskuem.aimondaimaker.core.util.getLocalVideoUrl

@Composable
actual fun getOnboardingVideoUrl(pageIndex: Int): String =
    when (pageIndex) {
        0 -> {
            getLocalVideoUrl(
                androidFileId = R.raw.onboarding_0,
            )
        }

        1 -> {
            getLocalVideoUrl(
                androidFileId = R.raw.onboarding_1,
            )
        }

        2 -> {
            getLocalVideoUrl(
                androidFileId = R.raw.onboarding_2,
            )
        }

        3 -> {
            getLocalVideoUrl(
                androidFileId = R.raw.onboarding_3,
            )
        }

        else -> {
            throw IllegalArgumentException("Invalid page index")
        }
    }
