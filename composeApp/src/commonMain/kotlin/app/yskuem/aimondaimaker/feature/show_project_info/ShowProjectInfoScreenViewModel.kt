package app.yskuem.aimondaimaker.feature.show_project_info

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import app.yskuem.aimondaimaker.domain.entity.Note
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
    private val noteRepository: NoteRepository,
    private val subscriptionRepository: SubscriptionRepository,
    private val projectId: String,
) : ScreenModel {
    private val _quizInfoList = MutableStateFlow<DataUiState<List<QuizInfo>>>(DataUiState.Loading)
    private val _noteList = MutableStateFlow<DataUiState<List<Note>>>(DataUiState.Loading)
    private val _selectedTabIndex = MutableStateFlow(0)
    private val _isSubscribed = MutableStateFlow(false)

    init {
        fetchQuizInfo()
        screenModelScope.launch {
            subscriptionRepository.isSubscribed().collect {
                _isSubscribed.value = it
            }
        }
    }

    val uiState: StateFlow<ProjectInfoScreenState> =
        combine(
            _quizInfoList,
            _noteList,
            _selectedTabIndex,
            _isSubscribed,
        ) { quizInfoList, noteList, selectedTabIndex, isSubscribed ->
            ProjectInfoScreenState(
                quizInfoList = quizInfoList,
                noteList = noteList,
                selectedTabIndex = selectedTabIndex,
                isSubscribed = isSubscribed,
            )
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProjectInfoScreenState(),
        )

    fun onTapQuizInfo(
        groupId: String,
        navigator: Navigator?,
    ) {
        screenModelScope.launch {
            val currentState = _quizInfoList.value
            _quizInfoList.value = DataUiState.Loading

            val res =
                runCatching {
                    quizRepository.fetchAnsweredQuizzes(groupId = groupId)
                }
            res
                .onSuccess { quizList ->
                    navigator?.push(ShowAnsweredQuizzesScreen(quizList = quizList))
                    _quizInfoList.value = currentState
                }.onFailure { exception ->
                    _quizInfoList.value = DataUiState.Error(exception)
                }
        }
    }

    private fun fetchQuizInfo() {
        screenModelScope.launch {
            val res =
                runCatching {
                    quizRepository.fetchQuizInfoList(
                        projectId = projectId,
                    )
                }
            res
                .onSuccess { quizInfoList ->
                    _quizInfoList.value = DataUiState.Success(quizInfoList)
                }.onFailure { exception ->
                    _quizInfoList.value = DataUiState.Error(exception)
                }
        }
    }

    private fun fetchNoteList() {
        screenModelScope.launch {
            // noteのfetchをする
            val result =
                runCatching {
                    noteRepository.fetchNotes(projectId = projectId)
                }
            result
                .onSuccess { notes ->
                    _noteList.value = DataUiState.Success(data = notes)
                }.onFailure {
                    _noteList.value = DataUiState.Error(it)
                }
        }
    }

    fun onTapTab(tabIndex: Int) {
        _selectedTabIndex.value = tabIndex
        if (tabIndex != 0) {
            fetchNoteList()
        }
    }

    fun refreshQuizInfo() {
        _quizInfoList.value = DataUiState.Loading
        fetchQuizInfo()
    }

    fun refreshNoteList() {
        _quizInfoList.value = DataUiState.Loading
        fetchNoteList()
    }

    fun deleteQuizInfo(quizInfoId: String) {
        screenModelScope.launch {
            val result =
                runCatching {
                    quizRepository.deleteQuizInfo(quizInfoId)
                }
            result
                .onSuccess { success ->
                    if (success) {
                        refreshQuizInfo()
                    }
                }.onFailure { exception ->
                    _quizInfoList.value = DataUiState.Error(exception)
                }
        }
    }

    fun deleteNote(noteId: String) {
        screenModelScope.launch {
            val result =
                runCatching {
                    noteRepository.deleteNote(noteId)
                }
            result
                .onSuccess { success ->
                    if (success) {
                        refreshNoteList()
                    }
                }.onFailure { exception ->
                    _noteList.value = DataUiState.Error(exception)
                }
        }
    }
}
