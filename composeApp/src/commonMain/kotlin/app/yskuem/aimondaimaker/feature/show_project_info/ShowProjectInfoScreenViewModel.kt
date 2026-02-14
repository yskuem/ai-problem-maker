package app.yskuem.aimondaimaker.feature.show_project_info

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.combine
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
    companion object {
        private const val PAGE_SIZE = 20
    }

    private val _quizInfoList = MutableStateFlow<DataUiState<List<QuizInfo>>>(DataUiState.Loading)
    private val _noteList = MutableStateFlow<DataUiState<List<Note>>>(DataUiState.Loading)
    private val _selectedTabIndex = MutableStateFlow(0)
    private val _isSubscribed = MutableStateFlow(false)

    // Pagination state for quiz
    private val _isLoadingMoreQuiz = MutableStateFlow(false)
    private val _hasMoreQuiz = MutableStateFlow(true)
    private var quizOffset = 0

    // Pagination state for note
    private val _isLoadingMoreNote = MutableStateFlow(false)
    private val _hasMoreNote = MutableStateFlow(true)
    private var noteOffset = 0

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
            _isLoadingMoreQuiz,
            _hasMoreQuiz,
            _isLoadingMoreNote,
            _hasMoreNote,
        ) {
            quizInfoList,
            noteList,
            selectedTabIndex,
            isSubscribed,
            isLoadingMoreQuiz,
            hasMoreQuiz,
            isLoadingMoreNote,
            hasMoreNote,
            ->
            ProjectInfoScreenState(
                quizInfoList = quizInfoList,
                noteList = noteList,
                selectedTabIndex = selectedTabIndex,
                isSubscribed = isSubscribed,
                isLoadingMoreQuiz = isLoadingMoreQuiz,
                hasMoreQuiz = hasMoreQuiz,
                isLoadingMoreNote = isLoadingMoreNote,
                hasMoreNote = hasMoreNote,
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
            quizOffset = 0
            _hasMoreQuiz.value = true
            val res =
                runCatching {
                    quizRepository.fetchQuizInfoList(
                        projectId = projectId,
                        limit = PAGE_SIZE,
                        offset = 0,
                    )
                }
            res
                .onSuccess { quizInfoList ->
                    _quizInfoList.value = DataUiState.Success(quizInfoList)
                    quizOffset = quizInfoList.size
                    _hasMoreQuiz.value = quizInfoList.size >= PAGE_SIZE
                }.onFailure { exception ->
                    _quizInfoList.value = DataUiState.Error(exception)
                }
        }
    }

    fun fetchMoreQuizInfo() {
        if (_isLoadingMoreQuiz.value || !_hasMoreQuiz.value) return
        val currentState = _quizInfoList.value
        if (currentState !is DataUiState.Success) return

        screenModelScope.launch {
            _isLoadingMoreQuiz.value = true
            val res =
                runCatching {
                    quizRepository.fetchQuizInfoList(
                        projectId = projectId,
                        limit = PAGE_SIZE,
                        offset = quizOffset,
                    )
                }
            res
                .onSuccess { newQuizInfoList ->
                    val allQuizInfoList = currentState.data + newQuizInfoList
                    _quizInfoList.value = DataUiState.Success(allQuizInfoList)
                    quizOffset += newQuizInfoList.size
                    _hasMoreQuiz.value = newQuizInfoList.size >= PAGE_SIZE
                }.onFailure { exception ->
                    println(exception)
                }
            _isLoadingMoreQuiz.value = false
        }
    }

    private fun fetchNoteList() {
        screenModelScope.launch {
            noteOffset = 0
            _hasMoreNote.value = true
            val result =
                runCatching {
                    noteRepository.fetchNotes(
                        projectId = projectId,
                        limit = PAGE_SIZE,
                        offset = 0,
                    )
                }
            result
                .onSuccess { notes ->
                    _noteList.value = DataUiState.Success(data = notes)
                    noteOffset = notes.size
                    _hasMoreNote.value = notes.size >= PAGE_SIZE
                }.onFailure {
                    _noteList.value = DataUiState.Error(it)
                }
        }
    }

    fun fetchMoreNotes() {
        if (_isLoadingMoreNote.value || !_hasMoreNote.value) return
        val currentState = _noteList.value
        if (currentState !is DataUiState.Success) return

        screenModelScope.launch {
            _isLoadingMoreNote.value = true
            val result =
                runCatching {
                    noteRepository.fetchNotes(
                        projectId = projectId,
                        limit = PAGE_SIZE,
                        offset = noteOffset,
                    )
                }
            result
                .onSuccess { newNotes ->
                    val allNotes = currentState.data + newNotes
                    _noteList.value = DataUiState.Success(data = allNotes)
                    noteOffset += newNotes.size
                    _hasMoreNote.value = newNotes.size >= PAGE_SIZE
                }.onFailure { exception ->
                    println(exception)
                }
            _isLoadingMoreNote.value = false
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
        _noteList.value = DataUiState.Loading
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
