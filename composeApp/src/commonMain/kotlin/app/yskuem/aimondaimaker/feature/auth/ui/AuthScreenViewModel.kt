package app.yskuem.aimondaimaker.feature.auth.ui

import app.yskuem.aimondaimaker.data.local_db.UserDataStore
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(AuthScreenState())
    val uiState: StateFlow<AuthScreenState> = _uiState.asStateFlow()

    fun login() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result =
                runCatching {
                    val currentUser = authRepository.getUser()
                    if (currentUser == null) {
                        authRepository.signInAnonymous()
                        userRepository.saveUser()
                        _uiState.update {
                            it.copy(isInitialLoginUser = true)
                        }
                    }
                    println("Login successful: ${authRepository.getUserId()}")
                }
            result
                .onSuccess {
                    _uiState.update {
                        it.copy(isLoginSuccessful = true)
                    }
                    println("Login successful")
                }.onFailure {
                    _uiState.update { state ->
                        state.copy(isLoginSuccessful = false)
                    }
                    println("Login failed: ${it.message}")
                }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
