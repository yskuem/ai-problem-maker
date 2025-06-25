package app.yskuem.aimondaimaker.feature.ad.config

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.getFlavor

actual fun getAdmobInterstitialId(): String =
    when (getFlavor()) {
        Flavor.DEV -> AdmobIosDevConfigs.INTERSTITIAL_ID
        Flavor.STAGING -> AdmobIosDevConfigs.INTERSTITIAL_ID
        Flavor.PROD -> AdmobIosProdConfigs.INTERSTITIAL_ID
    }

actual fun getAdmobBannerId(): String =
    when (getFlavor()) {
        Flavor.DEV -> AdmobIosDevConfigs.BANNER_ID
        Flavor.STAGING -> AdmobIosDevConfigs.BANNER_ID
        Flavor.PROD -> AdmobIosProdConfigs.BANNER_ID
    }
