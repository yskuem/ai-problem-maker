package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Note

interface NoteRepository {
    suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Note>
}