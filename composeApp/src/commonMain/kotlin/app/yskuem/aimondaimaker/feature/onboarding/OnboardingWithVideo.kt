package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.yskuem.aimondaimaker.core.ui.components.LoopingVideoPlayer
import io.github.yskuem.onboarding.api.DotsContainerStyle
import io.github.yskuem.onboarding.api.IntroductionScreen
import io.github.yskuem.onboarding.api.PageDecoration
import io.github.yskuem.onboarding.api.PageViewModel


@Composable
fun OnboardingWithVideo(
    onDone: () -> Unit = { }
) {
    val pages = listOf(
        PageViewModel(
            title = "See it in action",
            body = "This is a short looped preview.",
            image = {
                // オンボーディングの画像スロットに動画をそのまま埋め込む
                LoopingVideoPlayer(
                    url = getOnboardingVideoUrl(pageIndex = 0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(16.dp))
                )
            },
            decoration = PageDecoration(pageColor = Color.White)
        ),
        PageViewModel(
            title = "Auto-generated Quizzes",
            body = "Create quizzes from your notes and photos.",
            image = {
                LoopingVideoPlayer(
                    url = getOnboardingVideoUrl(pageIndex = 1),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(16.dp))
                )
            },
            decoration = PageDecoration(pageColor = Color.White)
        ),
        PageViewModel(
            title = "Get Started",
            body = "Track progress and keep your streak.",
            image = {
                LoopingVideoPlayer(
                    url = getOnboardingVideoUrl(pageIndex = 2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(16.dp))
                )
            },
            decoration = PageDecoration(pageColor = Color.White)
        ),
        PageViewModel(
            title = "Get Started",
            body = "Track progress and keep your streak.",
            image = {
                LoopingVideoPlayer(
                    url = getOnboardingVideoUrl(pageIndex = 3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(9f / 16f)
                        .clip(RoundedCornerShape(16.dp))
                )
            },
            decoration = PageDecoration(pageColor = Color.White)
        )
    )

    IntroductionScreen(
        pages = pages,
        showSkipButton = true,
        showNextButton = true,
        showBackButton = true,
        showDoneButton = true,
        skip = { Text("Skip") },
        next = { Text("Next") },
        back = { Text("Back") },
        done = { Text("Done") },
        onSkip = {},
        onDone = {
            onDone()
        },
        dotsContainerStyle = DotsContainerStyle(
            containerColor = Color.Transparent,
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
        )
    )
}