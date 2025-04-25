package app.yskuem.aimondaimaker.feature.problem.uiState

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.Problem

data class ProblemUiState(
    val problems: DataUiState<List<Problem>> = DataUiState.Loading,
    val currentProblemIndex: Int = 0,
)

