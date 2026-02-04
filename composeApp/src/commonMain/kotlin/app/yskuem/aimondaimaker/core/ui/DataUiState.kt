package app.yskuem.aimondaimaker.core.ui

sealed class DataUiState<out T> {
    data object Initial : DataUiState<Nothing>()

    data object Loading : DataUiState<Nothing>()

    data class Success<out T>(
        val data: T,
    ) : DataUiState<T>()

    data class Error(
        val throwable: Throwable,
    ) : DataUiState<Nothing>()

    val isLoading: Boolean
        get() = this is Loading
}

fun <T1, T2, R> combineDataUiStates(
    state1: DataUiState<T1>,
    state2: DataUiState<T2>,
    transform: (T1, T2) -> R,
): DataUiState<R> =
    when {
        state1 is DataUiState.Error -> {
            state1
        }

        state2 is DataUiState.Error -> {
            state2
        }

        state1 is DataUiState.Loading || state2 is DataUiState.Loading -> {
            DataUiState.Loading
        }

        state1 is DataUiState.Success && state2 is DataUiState.Success -> {
            DataUiState.Success(transform(state1.data, state2.data))
        }

        else -> {
            DataUiState.Initial
        }
    }

@Suppress("unused")
fun <T1, T2, T3, R> combineDataUiStates(
    state1: DataUiState<T1>,
    state2: DataUiState<T2>,
    state3: DataUiState<T3>,
    transform: (T1, T2, T3) -> R,
): DataUiState<R> =
    when {
        state1 is DataUiState.Error -> {
            state1
        }

        state2 is DataUiState.Error -> {
            state2
        }

        state3 is DataUiState.Error -> {
            state3
        }

        state1 is DataUiState.Loading || state2 is DataUiState.Loading || state3 is DataUiState.Loading -> {
            DataUiState.Loading
        }

        state1 is DataUiState.Success && state2 is DataUiState.Success && state3 is DataUiState.Success -> {
            DataUiState.Success(transform(state1.data, state2.data, state3.data))
        }

        else -> {
            DataUiState.Initial
        }
    }
