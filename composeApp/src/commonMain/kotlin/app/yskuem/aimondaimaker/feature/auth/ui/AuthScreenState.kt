package app.yskuem.aimondaimaker.feature.auth.ui

data class AuthScreenState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isInitialLoginUser: Boolean = false,
)
