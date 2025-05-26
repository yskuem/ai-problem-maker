package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import kotlinx.coroutines.delay

class AdUseCaseImpl(
    private val adRepository: AdRepository,
): AdUseCase {
    override suspend fun onInterstitialAdLoaded() {
        if(!adRepository.interstitialEnabled.value) {
            return
        }
        // iOSで表示されないときがあるので若干遅らせる
        delay(1500)
        adRepository.showInterstitialAd()
    }
}