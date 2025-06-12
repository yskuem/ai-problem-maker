package app.yskuem.aimondaimaker.feature.ad.config

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.getFlavor

actual fun getAdmobInterstitialId(): String =
    when (getFlavor()) {
        Flavor.DEV -> AdmobAndroidDevConfigs.INTERSTITIAL_ID
        Flavor.STAGING -> AdmobAndroidDevConfigs.INTERSTITIAL_ID
        Flavor.PROD -> AdmobAndroidProdConfigs.INTERSTITIAL_ID
    }

actual fun getAdmobBannerId(): String =
    when (getFlavor()) {
        Flavor.DEV -> AdmobAndroidDevConfigs.BANNER_ID
        Flavor.STAGING -> AdmobAndroidDevConfigs.BANNER_ID
        Flavor.PROD -> AdmobAndroidProdConfigs.BANNER_ID
    }
