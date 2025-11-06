package app.yskuem.aimondaimaker.feature.onboarding

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.onboarding_back
import ai_problem_maker.composeapp.generated.resources.onboarding_next
import ai_problem_maker.composeapp.generated.resources.onboarding_page1_body
import ai_problem_maker.composeapp.generated.resources.onboarding_page1_title
import ai_problem_maker.composeapp.generated.resources.onboarding_page2_body
import ai_problem_maker.composeapp.generated.resources.onboarding_page2_title
import ai_problem_maker.composeapp.generated.resources.onboarding_page3_body
import ai_problem_maker.composeapp.generated.resources.onboarding_page3_title
import ai_problem_maker.composeapp.generated.resources.onboarding_page4_body
import ai_problem_maker.composeapp.generated.resources.onboarding_page4_title
import ai_problem_maker.composeapp.generated.resources.onboarding_skip
import ai_problem_maker.composeapp.generated.resources.onboarding_start
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.yskuem.aimondaimaker.core.ui.components.LoopingVideoPlayer
import io.github.yskuem.onboarding.api.DotsContainerStyle
import io.github.yskuem.onboarding.api.IntroductionScreen
import io.github.yskuem.onboarding.api.PageDecoration
import io.github.yskuem.onboarding.api.PageViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingWithVideo(onDone: () -> Unit = { }) {
    val density = LocalDensity.current
    var screenHeightDp by remember { mutableStateOf(0.dp) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .onGloballyPositioned { coords ->
                    screenHeightDp = with(density) { coords.size.height.toDp() }
                },
    ) {
        val targetHeight = (screenHeightDp * 0.75f).let { if (it > 0.dp) it else 300.dp }
        val targetWidth = targetHeight * (9f / 16f)
        val videoModifier =
            Modifier
                .width(targetWidth)
                .height(targetHeight)
                .clip(RoundedCornerShape(16.dp))
                .padding(20.dp)

        val pages =
            listOf(
                PageViewModel(
                    titleWidget = {
                        Text(
                            text = stringResource(Res.string.onboarding_page1_title),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    body = stringResource(Res.string.onboarding_page1_body),
                    image = {
                        LoopingVideoPlayer(
                            url = getOnboardingVideoUrl(pageIndex = 0),
                            modifier = videoModifier,
                            isMuted = true,
                        )
                    },
                    decoration = PageDecoration(pageColor = Color.White),
                ),
                PageViewModel(
                    titleWidget = {
                        Text(
                            text = stringResource(Res.string.onboarding_page2_title),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    body = stringResource(Res.string.onboarding_page2_body),
                    image = {
                        LoopingVideoPlayer(
                            url = getOnboardingVideoUrl(pageIndex = 1),
                            modifier = videoModifier,
                            isMuted = true,
                        )
                    },
                    decoration = PageDecoration(pageColor = Color.White),
                ),
                PageViewModel(
                    titleWidget = {
                        Text(
                            text = stringResource(Res.string.onboarding_page3_title),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    body = stringResource(Res.string.onboarding_page3_body),
                    image = {
                        LoopingVideoPlayer(
                            url = getOnboardingVideoUrl(pageIndex = 2),
                            modifier = videoModifier,
                            isMuted = true,
                        )
                    },
                    decoration = PageDecoration(pageColor = Color.White),
                ),
                PageViewModel(
                    titleWidget = {
                        Text(
                            text = stringResource(Res.string.onboarding_page4_title),
                            color = Color.Black,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    body = stringResource(Res.string.onboarding_page4_body),
                    image = {
                        LoopingVideoPlayer(
                            url = getOnboardingVideoUrl(pageIndex = 3),
                            modifier = videoModifier,
                            isMuted = true,
                        )
                    },
                    decoration = PageDecoration(pageColor = Color.White),
                ),
            )

        IntroductionScreen(
            pages = pages,
            showSkipButton = true,
            showNextButton = true,
            showBackButton = true,
            showDoneButton = true,
            skip = { Text(stringResource(Res.string.onboarding_skip)) },
            next = { Text(stringResource(Res.string.onboarding_next)) },
            back = { Text(stringResource(Res.string.onboarding_back)) },
            done = { Text(stringResource(Res.string.onboarding_start)) },
            onSkip = {},
            onDone = { onDone() },
            dotsContainerStyle =
                DotsContainerStyle(
                    containerColor = Color.Transparent,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                ),
        )
    }
}
