package app.yskuem.aimondaimaker.feature.quiz.viewmodel

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.uiState.QuizUiState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShowQuizScreenViewModel(
    private val quizRepository: QuizRepository,
) : ScreenModel {

    private val _quizList = MutableStateFlow<DataUiState<List<Quiz>>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)


    val uiState: StateFlow<QuizUiState> = combine(
        _quizList, _currentQuizIndex,
    ) { quizList, currentQuizListIndex ->
        QuizUiState(
            quizList = quizList,
            currentQuizListIndex = currentQuizListIndex
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = QuizUiState()
    )

    fun onFetchQuizList(
        imageByte: ByteArray,
        fileName: String,
        extension: String
    ) {
        screenModelScope.launch {
            _quizList.value = DataUiState.Loading
            try {
                val quizList = quizRepository.fetchFromImage(
                    image = imageByte,
                    fileName = fileName,
                    extension = extension,
                )
                _quizList.value = DataUiState.Success(quizList)
            } catch (e: Exception) {
                _quizList.value = DataUiState.Error(e)
            }
        }
    }
}
