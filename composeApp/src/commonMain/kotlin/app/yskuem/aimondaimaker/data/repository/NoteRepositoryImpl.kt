package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.response.NoteApiDto
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.entity.Note

class NoteRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
): NoteRepository {
    override suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String
    ): Note {
        val response = HttpClient.postWithImage<NoteApiDto>(
            imageBytes = image,
            fileName = fileName,
            extension = extension,
            path = "/generate_note"
        )
        return response.toDomain()
    }
}