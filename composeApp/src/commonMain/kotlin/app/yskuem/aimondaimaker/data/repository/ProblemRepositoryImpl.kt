package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.api.response.QuizResponse
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz

class QuizRepositoryImpl(
    private val authRepository: AuthRepository,
    private val supabaseClientHelper: SupabaseClientHelper,
): QuizRepository {

    override suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Quiz> {
        val response = HttpClient.postWithImage<List<QuizResponse>>(
            imageBytes = image,
            fileName = fileName,
            extension = extension,
        )
        if(response.isEmpty()) {
            throw IllegalStateException("Response is empty")
        }
        return response.first().args.questions.map { it.toDomain() }
    }

    override suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String
    ) {
        val userId = authRepository.getUserId()
        if(userId == null) {
            throw IllegalStateException("userId is null")
        }
        val upLoadQuiz = quiz.copy(
            projectId = projectId,
            createdUserId = userId,
        )
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.Quiz.NAME,
            item = upLoadQuiz
        )
    }
}