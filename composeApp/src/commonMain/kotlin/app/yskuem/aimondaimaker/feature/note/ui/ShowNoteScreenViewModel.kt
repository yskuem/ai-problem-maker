package app.yskuem.aimondaimaker.feature.note.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import app.yskuem.aimondaimaker.domain.entity.Note
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShowNoteScreenViewModel(
    private val authRepository: AuthRepository,
    private val noteRepository: NoteRepository,
    private val projectRepository: ProjectRepository,
    private val crashlytics: FirebaseCrashlytics,
    private val subscriptionRepository: SubscriptionRepository,
) : ScreenModel {
    private val _note = MutableStateFlow<DataUiState<Note>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)
    private val _isSubscribed = MutableStateFlow(false)

    init {
        screenModelScope.launch {
            _isSubscribed.value = subscriptionRepository.isSubscribed()
        }
    }

    val uiState: StateFlow<NoteUiState> =
        combine(
            _note,
            _currentQuizIndex,
            _isSubscribed,
        ) { note, currentNoteIndex, isSubscribed ->
            NoteUiState(
                note = note,
                currentNoteIndex = currentNoteIndex,
                isSubscribed = isSubscribed,
            )
        }.stateIn(
            scope = screenModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NoteUiState(),
        )

    fun onLoadPage(
        imageByte: ByteArray,
        fileName: String,
        extension: String,
        projectId: String? = null,
    ) {
        screenModelScope.launch {
            _note.value = DataUiState.Loading

            // 画像からNoteを取得
            val noteResult =
                onFetchNote(
                    imageByte = imageByte,
                    fileName = fileName,
                    extension = extension,
                )
            noteResult
                .onSuccess { note ->
                    _note.value = DataUiState.Success(note)
                    onSaveData(
                        note = note,
                        projectId = projectId,
                    )
                }.onFailure { e ->
                    crashlytics.log(e.toString())
                    _note.value = DataUiState.Error(e)
                }
        }
    }

    private suspend fun onFetchNote(
        imageByte: ByteArray,
        fileName: String,
        extension: String,
    ): Result<Note> =
        runCatching {
            noteRepository.generateFromImage(imageByte, fileName, extension)
        }

    private suspend fun onSaveData(
        note: Note,
        projectId: String? = null,
    ) {
        val res =
            runCatching {
                val userId = authRepository.getUserId()

                // 新規の場合はプロジェクトの作成
                val finalProjectId =
                    projectId ?: projectRepository
                        .addProject(
                            projectName = note.title,
                        ).id

                // Noteの保存
                noteRepository.saveNote(
                    projectId = finalProjectId,
                    userId = userId,
                    note = note,
                )
            }
        res
            .onSuccess {
                println("success save")
            }.onFailure { e ->
                println("$e")
            }
    }
}
