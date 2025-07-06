package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdRepositoryImpl : AdRepository {
    private val _interstitialEnabled = MutableStateFlow(false)
    override val interstitialEnabled: StateFlow<Boolean> = _interstitialEnabled.asStateFlow()

    override fun loadInterstitialAds() {
        // No-op for iOS - AdMob is not available during tests
    }

    override fun showInterstitialAd() {
        // No-op for iOS - AdMob is not available during tests
    }
}
