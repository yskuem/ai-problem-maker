package app.yskuem.aimondaimaker.data.repository

import app.lexilabs.basic.ads.Consent
import app.lexilabs.basic.ads.ConsentDebugSettings
import app.lexilabs.basic.ads.ConsentRequestParameters
import app.lexilabs.basic.ads.DependsOnGoogleMobileAds
import app.lexilabs.basic.ads.DependsOnGoogleUserMessagingPlatform
import app.lexilabs.basic.ads.ExperimentalBasicAds
import app.lexilabs.basic.ads.InterstitialAd
import app.yskuem.aimondaimaker.core.util.ContextFactory
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import app.yskuem.aimondaimaker.feature.ad.config.getAdmobInterstitialId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(DependsOnGoogleMobileAds::class, DependsOnGoogleUserMessagingPlatform::class, ExperimentalBasicAds::class)
class AdRepositoryImpl(
    contextFactory: ContextFactory
) : AdRepository {

    private val interstitial = InterstitialAd(contextFactory.getActivity())

    private val _interstitialEnabled = MutableStateFlow(false)
    override val interstitialEnabled: StateFlow<Boolean> = _interstitialEnabled.asStateFlow()


    private val consent = Consent(contextFactory.getActivity())

    private val _userConsented = MutableStateFlow(false)


    private val debugSettings = ConsentDebugSettings.Builder(contextFactory.getActivity()).build()

    private val params = ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()

    init {
        checkForConsent()
        loadInterstitialAds()
    }

    override fun loadInterstitialAds() {
        interstitial.load(
            adUnitId = getAdmobInterstitialId(),
            onLoad = {
                _interstitialEnabled.value = true
                setInterstitialListeners()
            },
            onFailure = { error ->
                println("ロードエラー$error")
            }
        )
    }

    private fun setInterstitialListeners() {
        interstitial.setListeners(
            onFailure = { error ->
                println("$error")
            },
            onDismissed = {
                // ユーザーが広告を閉じたら次のロードを行う
                _interstitialEnabled.value = false
                loadInterstitialAds()
            }
        )
    }

    override fun showInterstitialAd() {
        if (interstitialEnabled.value) {
            _interstitialEnabled.value = false
            interstitial.show()
        } else {
            loadInterstitialAds()
        }
    }


    private fun checkForConsent() {
        consent.requestConsentInfoUpdate(params) {
            //Log.e("AdRepo", "Consent update failed: $it")
        }
        _userConsented.value = consent.canRequestAds()
    }
}
