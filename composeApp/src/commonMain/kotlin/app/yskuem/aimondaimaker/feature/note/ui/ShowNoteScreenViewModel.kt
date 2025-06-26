package app.yskuem.aimondaimaker.feature.note.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import dev.gitlive.firebase.crashlytics.FirebaseCrashlytics
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
    private val adUseCase: AdUseCase,
    private val crashlytics: FirebaseCrashlytics,
) : ScreenModel {
    private val _note = MutableStateFlow<DataUiState<Note>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)

    val uiState: StateFlow<NoteUiState> =
        combine(
            _note,
            _currentQuizIndex,
        ) { note, currentNoteIndex ->
            NoteUiState(
                note = note,
                currentNoteIndex = currentNoteIndex,
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
                onFetchQuizList(
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

    fun showInterstitialAd() {
        screenModelScope.launch {
            // 広告表示
            adUseCase.onInterstitialAdLoaded()
        }
    }

    private suspend fun onFetchQuizList(
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
