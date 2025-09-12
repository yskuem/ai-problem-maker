package app.yskuem.aimondaimaker.feature.ad.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.InterstitialAdHandler
import app.lexilabs.basic.ads.composable.rememberInterstitialAd

@OptIn(markerClass = [DependsOnGoogleMobileAds::class])
@Composable
actual fun rememberPlatformInterstitialAd(): InterstitialAdHandler? {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() } ?: return null
    val interstitial by rememberInterstitialAd(activity)
    return interstitial
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}