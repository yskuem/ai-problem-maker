package app.yskuem.aimondaimaker.feature.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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


@Composable
fun OnboardingWithVideo(
    onDone: () -> Unit = { }
) {

    val density = LocalDensity.current
    var screenHeightDp by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                screenHeightDp = with(density) { coords.size.height.toDp() }
            }
    ) {

        val targetHeight = (screenHeightDp * 0.75f).let { if (it > 0.dp) it else 300.dp }
        val targetWidth = targetHeight * (9f / 16f)
        val videoModifier = Modifier
            .width(targetWidth)
            .height(targetHeight)
            .clip(RoundedCornerShape(16.dp))
            .padding(20.dp)

        val pages = listOf(
            PageViewModel(
                titleWidget = {
                    Text(
                        text = "写真から復習問題を自動作成！",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                body = "参考書やノートの写真を撮るだけで、AIが自動でその内容に基づいた復習問題を作成します。",
                image = {
                    LoopingVideoPlayer(
                        url = getOnboardingVideoUrl(pageIndex = 0),
                        modifier = videoModifier
                    )
                },
                decoration = PageDecoration(pageColor = Color.White)
            ),
            PageViewModel(
                titleWidget = {
                    Text(
                        text = "写真から電子ノートを自動作成！",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                body = "参考書やノートの写真を撮るだけで、AIが自動でその内容に基づいた電子ノートを作成します。",
                image = {
                    LoopingVideoPlayer(
                        url = getOnboardingVideoUrl(pageIndex = 1),
                        modifier = videoModifier
                    )
                },
                decoration = PageDecoration(pageColor = Color.White)
            ),
            PageViewModel(
                titleWidget = {
                    Text(
                        text = "参考書を持ち歩かないで電車の中でも勉強ができる！",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                body = "AIが作成した復習問題と電子ノートをスマホでいつでもどこでも確認できます。",
                image = {
                    LoopingVideoPlayer(
                        url = getOnboardingVideoUrl(pageIndex = 2),
                        modifier = videoModifier
                    )
                },
                decoration = PageDecoration(pageColor = Color.White)
            ),
            PageViewModel(
                titleWidget = {
                    Text(
                        text = "点数が上がって試験に合格！",
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                body = "AIが作成した復習問題や電子ノートで効率的に学習し、試験で高得点を目指しましょう！",
                image = {
                    LoopingVideoPlayer(
                        url = getOnboardingVideoUrl(pageIndex = 3),
                        modifier = videoModifier
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
            onDone = { onDone() },
            dotsContainerStyle = DotsContainerStyle(
                containerColor = Color.Transparent,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
            )
        )
    }
}
