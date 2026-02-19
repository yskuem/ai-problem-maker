package app.yskuem.aimondaimaker.feature.onboarding

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WelcomeUiState(
    val isLoading: Boolean = false,
    val isSocialSigningIn: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String? = null,
)

class WelcomeScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    init {
        observeSessionChanges()
    }

    private fun observeSessionChanges() {
        screenModelScope.launch {
            authRepository.observeSessionStatus().collect { status ->
                if (status is SessionStatus.Authenticated && _uiState.value.isSocialSigningIn) {
                    // ソーシャルログイン完了 → ユーザー保存
                    val result = runCatching { userRepository.saveUser() }
                    result
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isSocialSigningIn = false,
                                    isLoading = false,
                                    loginSuccess = true,
                                )
                            }
                        }
                        .onFailure { e ->
                            _uiState.update {
                                it.copy(
                                    isSocialSigningIn = false,
                                    isLoading = false,
                                    loginError = e.message ?: "Unknown error",
                                )
                            }
                        }
                }
            }
        }
    }

    fun startAnonymous() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }
            val result = runCatching {
                authRepository.signInAnonymous()
                userRepository.saveUser()
            }
            result
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, loginError = e.message ?: "Unknown error")
                    }
                }
        }
    }

    fun signInWithGoogle() {
        screenModelScope.launch {
            _uiState.update {
                it.copy(isSocialSigningIn = true, isLoading = true, loginError = null)
            }
            runCatching { authRepository.signInWithGoogle() }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSocialSigningIn = false,
                            isLoading = false,
                            loginError = e.message ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun signInWithApple() {
        screenModelScope.launch {
            _uiState.update {
                it.copy(isSocialSigningIn = true, isLoading = true, loginError = null)
            }
            runCatching { authRepository.signInWithApple() }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isSocialSigningIn = false,
                            isLoading = false,
                            loginError = e.message ?: "Unknown error",
                        )
                    }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(loginError = null) }
    }
}
