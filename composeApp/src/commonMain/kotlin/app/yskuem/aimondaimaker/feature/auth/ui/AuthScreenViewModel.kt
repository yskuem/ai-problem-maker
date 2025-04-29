package app.yskuem.aimondaimaker.feature.auth.ui

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthScreenViewModel(
    private val authRepository: AuthRepository
): ScreenModel {
    private val _hasError = MutableStateFlow(false)
    private val _isLoginSuccess = MutableStateFlow(false)
    val hasError: StateFlow<Boolean> = _hasError.asStateFlow()
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess.asStateFlow()
    fun login() {
        screenModelScope.launch {
            val result = runCatching {
                val currentUser = authRepository.getUser()
                if (currentUser == null) {
                    authRepository.signInAnonymous()
                }
            }
            result.onSuccess {
                _isLoginSuccess.value = true
                println("Login successful")
            }.onFailure {
                _hasError.value = true
                println("Login failed: ${it.message}")
            }
        }
    }
}