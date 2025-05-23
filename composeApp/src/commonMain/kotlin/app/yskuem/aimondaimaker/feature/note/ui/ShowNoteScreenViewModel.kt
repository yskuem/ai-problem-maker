package app.yskuem.aimondaimaker.feature.note.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
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
) : ScreenModel {

    private val _note = MutableStateFlow<DataUiState<Note>>(DataUiState.Loading)
    private val _currentQuizIndex = MutableStateFlow(0)


    val uiState: StateFlow<NoteUiState> = combine(
        _note, _currentQuizIndex,
    ) { note, currentNoteIndex ->
        NoteUiState(
            note = note,
            currentNoteIndex = currentNoteIndex
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NoteUiState()
    )

    fun onLoadPage(
        imageByte: ByteArray,
        fileName: String,
        extension: String
    ) {
        screenModelScope.launch {
            _note.value = DataUiState.Loading

            // 画像からNoteを取得
            val noteResult = onFetchQuizList(
                imageByte = imageByte,
                fileName = fileName,
                extension = extension,
            )
            noteResult.onSuccess { note ->
                _note.value = DataUiState.Success(note)
                onSaveData(note)
            }
            .onFailure {
                _note.value = DataUiState.Error(it)
            }
        }
    }

    private suspend fun onFetchQuizList(
        imageByte: ByteArray,
        fileName: String,
        extension: String
    ): Result<Note> {
        return runCatching {
            noteRepository.generateFromImage(imageByte, fileName, extension)
        }
    }

    private suspend fun onSaveData(
        note: Note,
    ) {
        val res = runCatching {
            // Projectの保存
            // TODO
            val project = projectRepository.addProject(
                projectName = note.title
            )

            val userId = authRepository.getUserId()

            // Noteの保存
            noteRepository.saveNote(
                projectId = project.id,
                userId = userId,
                note = note,
            )
        }
        res.onSuccess {
            println("success save")
        }.onFailure { e ->
            println("$e")
        }
    }
}