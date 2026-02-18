package app.yskuem.aimondaimaker.feature.settings

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(
    val isLinking: Boolean = false,
    val linkSuccess: Boolean = false,
    val linkError: String? = null,
    val isAnonymous: Boolean = true,
    val linkedProvider: String? = null,
    val linkedEmail: String? = null,
)

class SettingsScreenViewModel(
    private val authRepository: AuthRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        checkAccountStatus()
    }

    private fun checkAccountStatus() {
        val isAnonymous = authRepository.isAnonymousUser()
        _uiState.update {
            it.copy(
                isAnonymous = isAnonymous,
                linkedProvider = if (!isAnonymous) authRepository.getLinkedProviderName() else null,
                linkedEmail = if (!isAnonymous) authRepository.getLinkedEmail() else null,
            )
        }
    }

    fun linkWithGoogle() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLinking = true, linkError = null, linkSuccess = false) }
            val result = runCatching { authRepository.linkWithGoogle() }
            result
                .onSuccess {
                    checkAccountStatus()
                    _uiState.update {
                        it.copy(
                            isLinking = false,
                            linkSuccess = true,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLinking = false,
                            linkError = e.message ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun linkWithApple() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLinking = true, linkError = null, linkSuccess = false) }
            val result = runCatching { authRepository.linkWithApple() }
            result
                .onSuccess {
                    checkAccountStatus()
                    _uiState.update {
                        it.copy(
                            isLinking = false,
                            linkSuccess = true,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLinking = false,
                            linkError = e.message ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(linkSuccess = false, linkError = null) }
    }
}
