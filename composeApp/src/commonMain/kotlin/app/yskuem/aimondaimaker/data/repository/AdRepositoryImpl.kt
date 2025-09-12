package app.yskuem.aimondaimaker.data.repository

import app.lexilabs.basic.ads.Consent
import app.lexilabs.basic.ads.ConsentDebugSettings
import app.lexilabs.basic.ads.ConsentRequestParameters
import app.lexilabs.basic.ads.DependsOnGoogleUserMessagingPlatform
import app.lexilabs.basic.ads.ExperimentalBasicAds
import app.yskuem.aimondaimaker.core.util.ContextFactory
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(
    DependsOnGoogleUserMessagingPlatform::class,
    ExperimentalBasicAds::class
)
class AdRepositoryImpl(
    private val contextFactory: ContextFactory,
) : AdRepository {

    private val _interstitialRequests =
        MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val interstitialRequests: SharedFlow<Unit> = _interstitialRequests

    private val consent = Consent(contextFactory.getActivity())

    private val debugSettings =
        ConsentDebugSettings.Builder(contextFactory.getActivity()).build()
    private val params =
        ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()

    private val _userConsented = MutableStateFlow(false)
    val userConsented: StateFlow<Boolean> = _userConsented

    init { checkForConsent() }

    fun requestInterstitial() {
        _interstitialRequests.tryEmit(Unit)
    }

    private fun checkForConsent() {
        consent.requestConsentInfoUpdate(params) { /* log if needed */ }
        _userConsented.value = consent.canRequestAds  // ← 0.2.7 ではプロパティ
    }
}

