package app.yskuem.aimondaimaker.feature.ad.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.InterstitialAdHandler
import app.lexilabs.basic.ads.composable.rememberInterstitialAd
import app.yskuem.aimondaimaker.core.util.findActivity
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobInterstitialId

@OptIn(markerClass = [DependsOnGoogleMobileAds::class])
@Composable
actual fun rememberPlatformInterstitialAd(): InterstitialAdHandler? {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() } ?: return null
    val interstitial by rememberInterstitialAd(
        adUnitId = getAdmobInterstitialId(),
        activity = activity,
    )
    return interstitial
}
