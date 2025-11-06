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
import ai_problem_maker.composeapp.generated.resources.onboarding_toggle_sound
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var isMuted by remember { mutableStateOf(true) }

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
        val videoContainerModifier =
            Modifier
                .width(targetWidth)
                .height(targetHeight)
                .padding(20.dp)

        val onboardingVideo: @Composable (String) -> Unit = { url ->
            OnboardingVideoPlayer(
                url = url,
                containerModifier = videoContainerModifier,
                isMuted = isMuted,
                onToggleMute = { isMuted = !isMuted },
            )
        }

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
                        onboardingVideo(getOnboardingVideoUrl(pageIndex = 0))
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
                        onboardingVideo(getOnboardingVideoUrl(pageIndex = 1))
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
                        onboardingVideo(getOnboardingVideoUrl(pageIndex = 2))
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
                        onboardingVideo(getOnboardingVideoUrl(pageIndex = 3))
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

@Composable
private fun OnboardingVideoPlayer(
    url: String,
    containerModifier: Modifier,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
) {
    val toggleSoundDescription = stringResource(Res.string.onboarding_toggle_sound)

    Box(modifier = containerModifier) {
        LoopingVideoPlayer(
            url = url,
            modifier = Modifier.clip(RoundedCornerShape(16.dp)),
            isMuted = isMuted,
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(32.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.6f),
            contentColor = Color.White,
        ) {
            IconButton(onClick = onToggleMute) {
                Icon(
                    imageVector =
                        if (isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = toggleSoundDescription,
                )
            }
        }
    }
}
