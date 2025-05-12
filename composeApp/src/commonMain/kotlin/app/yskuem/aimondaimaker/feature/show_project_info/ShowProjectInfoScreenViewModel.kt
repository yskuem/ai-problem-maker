package app.yskuem.aimondaimaker.feature.show_project_info

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.NoteInfo
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
import app.yskuem.aimondaimaker.feature.quiz.ui.ShowAnsweredQuizzesScreen
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShowProjectInfoScreenViewModel(
    private val quizRepository: QuizRepository,
    private val authRepository: AuthRepository,
) : ScreenModel {
    private val _quizInfoList = MutableStateFlow<DataUiState<List<QuizInfo>>>(DataUiState.Loading)
    private val _noteInfoList = MutableStateFlow<DataUiState<List<NoteInfo>>>(DataUiState.Loading)

    init {
        fetchQuizInfo()
    }


    val uiState: StateFlow<ProjectInfoScreenState> = combine(
        _quizInfoList, _noteInfoList,
    ) { quizInfoList, currentQuizListIndex ->
        ProjectInfoScreenState(
            quizInfoList = quizInfoList,
            noteInfoList = currentQuizListIndex
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProjectInfoScreenState()
    )

    fun onTapQuizInfo(
        projectId: String,
        navigator: Navigator?,
    ) {
        screenModelScope.launch {
            val currentState = _quizInfoList.value
            _quizInfoList.value = DataUiState.Loading

            val res = runCatching {
                quizRepository.fetchAnsweredQuizList(projectId = projectId)
            }
            res.onSuccess { quizList ->
                navigator?.push(ShowAnsweredQuizzesScreen(quizList = quizList))
                _quizInfoList.value = currentState
            }.onFailure { exception ->
                _quizInfoList.value = DataUiState.Error(exception)
            }
        }
    }

    private fun fetchQuizInfo() {
        screenModelScope.launch {
            val res = runCatching {
                quizRepository.fetchQuizInfoList(
                    userId = authRepository.getUserId()
                )
            }
            res.onSuccess { quizInfoList ->
                _quizInfoList.value = DataUiState.Success(quizInfoList)
            }.onFailure { exception ->
                _quizInfoList.value = DataUiState.Error(exception)
            }
        }
    }
}