package app.yskuem.aimondaimaker.feature.subscription

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.revenuecat.purchases.kmp.models.Package
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionScreenViewModel(
    private val authRepository: AuthRepository,
    private val subscriptionRepository: SubscriptionRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(SubscriptionScreenState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SubscriptionScreenEvent) {
        screenModelScope.launch {
            when(event) {
                is SubscriptionScreenEvent.Initialize -> initialize()
                is SubscriptionScreenEvent.RestorePurchase -> restore()
                is SubscriptionScreenEvent.PurchaseSubscription -> purchaseSubscription(
                    purchasePackage = event.purchasePackage
                )
            }
        }
    }

    private fun initialize() {
        RevenueCatInitializer.configureIfNeeded()
        screenModelScope.launch {
            fetchOffering()
        }
        screenModelScope.launch {
            checkIsSubscribed()
        }

    }

    private suspend fun fetchOffering() {
        runCatching {
            subscriptionRepository.identifyUser(
                appUserId = authRepository.getUserId()
            )
            subscriptionRepository.fetchCurrentOfferingOrNull()
                ?: throw NoSuchElementException("No offering available")
        }.fold(
            onSuccess = { offering ->
                _uiState.update {
                    it.copy(
                        offering = DataUiState.Success(offering)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        offering = DataUiState.Error(error)
                    )
                }
            }
        )
    }

    private suspend fun checkIsSubscribed() {
        changeToLoadingState()
        return runCatching {
            subscriptionRepository.isSubscribed()
        }.fold(
            onSuccess = { isSubscribed ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Success(isSubscribed)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Error(error)
                    )
                }
            }
        )
    }

    private suspend fun purchaseSubscription(purchasePackage: Package) {
        changeToLoadingState()
        runCatching {
            subscriptionRepository.subscribe(packageToPurchase = purchasePackage)
        }.fold(
            onSuccess = { _ ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Success(true)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Error(error)
                    )
                }
            }
        )
    }

    private suspend fun restore() {
        changeToLoadingState()
        runCatching {
            subscriptionRepository.restorePurchaseAndRecheckIsSubscribed()
        }.fold(
            onSuccess = { isSubscribed ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Success(isSubscribed)
                    )
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        isSubscribed = DataUiState.Error(error)
                    )
                }
            }
        )
    }

    private fun changeToLoadingState() {
        _uiState.update {
            it.copy(
                isSubscribed = DataUiState.Loading,
            )
        }
    }
}