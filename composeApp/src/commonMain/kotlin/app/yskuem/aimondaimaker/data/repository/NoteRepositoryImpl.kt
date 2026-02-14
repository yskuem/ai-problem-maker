package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.response.NoteApiDto
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.response.NoteSupabaseDto
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.entity.Note

class NoteRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
) : NoteRepository {
    override suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): Note {
        val response =
            HttpClient.postWithImage<NoteApiDto>(
                imageBytes = image,
                fileName = fileName,
                extension = extension,
                path = "/generate_note",
            )
        return response.toDomain()
    }

    override suspend fun fetchNotes(projectId: String): List<Note> {
        val res =
            supabaseClientHelper.fetchListByMatchValue<NoteSupabaseDto>(
                tableName = SupabaseTableName.Note.NAME,
                filterCol = SupabaseColumnName.PROJECT_ID,
                filterVal = projectId,
                orderCol = SupabaseColumnName.CREATED_AT,
            )
        return res.map { it.toDomain() }
    }

    override suspend fun fetchNotes(projectId: String, limit: Int, offset: Int): List<Note> {
        val res =
            supabaseClientHelper.fetchPaginatedListByMatchValue<NoteSupabaseDto>(
                tableName = SupabaseTableName.Note.NAME,
                filterCol = SupabaseColumnName.PROJECT_ID,
                filterVal = projectId,
                orderCol = SupabaseColumnName.CREATED_AT,
                limit = limit.toLong(),
                offset = offset.toLong(),
            )
        return res.map { it.toDomain() }
    }

    override suspend fun saveNote(
        note: Note,
        projectId: String,
        userId: String,
    ) {
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.Note.NAME,
            item =
                note.toDTO(
                    projectId = projectId,
                    createdUserId = userId,
                ),
        )
    }

    override suspend fun deleteNote(noteId: String): Boolean =
        supabaseClientHelper.deleteItemById(
            tableName = SupabaseTableName.Note.NAME,
            idCol = "id",
            idVal = noteId,
        )

    override suspend fun deleteNotesByProjectId(projectId: String): Boolean =
        supabaseClientHelper.deleteItemsByMatch(
            tableName = SupabaseTableName.Note.NAME,
            filterCol = SupabaseColumnName.PROJECT_ID,
            filterVal = projectId,
        )
}
