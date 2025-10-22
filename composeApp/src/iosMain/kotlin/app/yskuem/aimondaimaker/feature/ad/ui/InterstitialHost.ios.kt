package app.yskuem.aimondaimaker.feature.ad.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.InterstitialAdHandler
import app.lexilabs.basic.ads.composable.rememberInterstitialAd
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobInterstitialId
import platform.UIKit.*
import platform.UIKit.UIApplication
import platform.UIKit.UISceneActivationStateForegroundActive
import platform.UIKit.UIViewController
import platform.UIKit.UIWindowScene

@OptIn(markerClass = [DependsOnGoogleMobileAds::class])
@Composable
actual fun rememberPlatformInterstitialAd(): InterstitialAdHandler? {
    val vc = remember { currentRootViewController() } ?: return null
    val interstitial by rememberInterstitialAd(
        activity = vc,
        adUnitId = getAdmobInterstitialId(),
    )
    return interstitial
}

private fun currentRootViewController(): UIViewController? {
    val scenes = UIApplication.sharedApplication.connectedScenes
    val scene =
        scenes.firstOrNull {
            (it as? UIWindowScene)?.activationState == UISceneActivationStateForegroundActive
        } as? UIWindowScene ?: return null

    val windows: List<UIWindow> =
        scene.windows.filterIsInstance<UIWindow>()

    val key = windows.firstOrNull { it.isKeyWindow() } ?: windows.firstOrNull()
    return key?.rootViewController
}
