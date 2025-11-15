package app.yskuem.aimondaimaker.core.ui.components

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.ic_splash_back
import ai_problem_maker.composeapp.generated.resources.ic_splash_icon
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.alexzhirkevich.compottie.ExperimentalCompottieApi
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.github.alexzhirkevich.compottie.rememberResourcesAssetsManager
import io.github.alexzhirkevich.compottie.rememberResourcesFontManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

/**
 * Lottie JSON を compose-resources から読み込み、1回再生して終了時に onFinished を呼ぶ。
 * @param resourcePath composeResources/src/commonMain/resources/ 以下の相対パス
 * @param modifier レイアウト調整用
 * @param background 背景色（スプラッシュの下地）
 * @param contentScale 画像の伸縮方法（必要に応じて Image の contentScale を渡す）
 * @param onFinished 再生完了時に1度だけ呼ばれる
 */
@Composable
fun SplashLottie(
    resourcePath: String = "files/ai_note_scan_splash_lottie_large.json", // ← 大きい版をデフォルトに
    modifier: Modifier = Modifier.fillMaxSize(),
    background: Color = Color.White,
    onFinished: () -> Unit = {}
) {
    // Lottie読み込み
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes(resourcePath).decodeToString()
        )
    }

    // 1回再生
    val progress by animateLottieCompositionAsState(composition)

    // 完了コールバック（1回だけ）
    var doneSent by remember { mutableStateOf(false) }
    LaunchedEffect(progress) {
        if (!doneSent && progress >= 1f) {
            doneSent = true
            onFinished()
        }
    }

    // 描画：横幅一杯に拡大
    Box(
        contentAlignment = Alignment.Center
    ) {
        val painter = rememberLottiePainter(
            composition = composition,
            progress = { progress }
        )
        Image(
            painter = painter,
            contentDescription = "Splash animation",
            modifier = Modifier.fillMaxWidth(),             // 横幅いっぱい
            contentScale = ContentScale.FillWidth           // 幅基準でスケール
        )
    }
}



