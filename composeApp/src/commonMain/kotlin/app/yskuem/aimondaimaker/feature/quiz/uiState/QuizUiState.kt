package app.yskuem.aimondaimaker.feature.quiz.uiState

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.domain.entity.Quiz

data class QuizUiState(
    val quizList: DataUiState<List<Quiz>> = DataUiState.Loading,
    val currentQuizListIndex: Int = 0,
    val pdfData: DataUiState<PdfResponse> = DataUiState.Loading,
    val pdfSaveState: DataUiState<Unit> = DataUiState.Initial,
)
