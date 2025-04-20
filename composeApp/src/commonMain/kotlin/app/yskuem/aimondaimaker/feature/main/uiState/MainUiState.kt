package app.yskuem.aimondaimaker.feature.main.uiState

import app.yskuem.aimondaimaker.domain.entity.Problem

data class MainUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val problems: List<Problem> = emptyList()
)
