package app.yskuem.aimondaimaker.feature.problem.uiState

import app.yskuem.aimondaimaker.domain.entity.Problem

data class ProblemUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val problems: List<Problem> = emptyList()
)
