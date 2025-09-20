package app.yskuem.aimondaimaker.feature.ad.ui

import androidx.compose.runtime.*
import app.lexilabs.basic.ads.AdState
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.InterstitialAdHandler
import app.lexilabs.basic.ads.composable.InterstitialAd


@OptIn(DependsOnGoogleMobileAds::class)
@Composable
fun InterstitialHost(
    onDismissed: () -> Unit = {}
) {
    val interstitial = rememberPlatformInterstitialAd() ?: return
    val onDismissedState by rememberUpdatedState(onDismissed)
    val isFinished = remember { mutableStateOf(false) }

    if (interstitial.state == AdState.READY && !isFinished.value) {
        InterstitialAd(
            interstitial,
            onDismissed = {
                onDismissedState()
                isFinished.value = true
            }
        )
    }
}


/** プラットフォーム毎に Interstitial のハンドラを用意して返す薄いシム */
@OptIn(DependsOnGoogleMobileAds::class)
@Composable
expect fun rememberPlatformInterstitialAd(): InterstitialAdHandler?