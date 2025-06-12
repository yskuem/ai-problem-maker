package app.yskuem.aimondaimaker.domain.data.repository

import kotlinx.coroutines.flow.StateFlow

interface AdRepository {
    val interstitialEnabled: StateFlow<Boolean>

    fun loadInterstitialAds()

    fun showInterstitialAd()
}
