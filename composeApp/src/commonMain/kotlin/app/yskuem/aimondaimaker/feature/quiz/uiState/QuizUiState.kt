package app.yskuem.aimondaimaker.feature.quiz.uiState

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.Quiz

data class QuizUiState(
    val quizList: DataUiState<List<Quiz>> = DataUiState.Loading,
    val currentQuizListIndex: Int = 0,
)

