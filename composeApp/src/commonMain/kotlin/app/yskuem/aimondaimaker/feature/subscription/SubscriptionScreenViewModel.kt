package app.yskuem.aimondaimaker.feature.subscription

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
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
            }
        }
    }

    private suspend fun initialize() {
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
}