package app.yskuem.aimondaimaker.feature.show_project_info

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.QuizInfo

data class ProjectInfoScreenState(
    val quizInfoList: DataUiState<List<QuizInfo>> = DataUiState.Loading,
    val noteList: DataUiState<List<Note>> = DataUiState.Loading,
)
