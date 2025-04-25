package app.yskuem.aimondaimaker.core.ui

sealed class DataUiState<out T> {
    data object Loading : DataUiState<Nothing>()
    data class Success<out T>(val data: T) : DataUiState<T>()
    data class Error(val throwable: Throwable) : DataUiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading
}