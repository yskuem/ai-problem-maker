package app.yskuem.aimondaimaker.feature.note.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.Quiz

data class NoteUiState(
    val note: DataUiState<Note> = DataUiState.Loading,
    val currentNoteIndex: Int = 0,
)
