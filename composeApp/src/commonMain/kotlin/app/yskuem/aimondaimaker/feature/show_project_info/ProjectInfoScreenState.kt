package app.yskuem.aimondaimaker.feature.show_project_info

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.NoteInfo
import app.yskuem.aimondaimaker.domain.entity.QuizInfo

data class ProjectInfoScreenState(
    val quizInfoList: DataUiState<List<QuizInfo>> = DataUiState.Loading,
    val noteInfoList: DataUiState<List<NoteInfo>> = DataUiState.Loading,
)
