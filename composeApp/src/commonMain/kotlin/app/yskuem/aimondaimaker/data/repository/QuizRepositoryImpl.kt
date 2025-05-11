package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.api.response.QuizResponse
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.response.QuizInfoDto
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
import kotlinx.datetime.Clock

class QuizRepositoryImpl(
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
        return response.first().args.map { it.toDomain() }
    }

    override suspend fun saveQuizInfo(
        projectId: String,
        userId: String,
        groupId: String,
        quizTitle: String,
    ) {
        val quizInfo = QuizInfo(
            projectId = projectId,
            groupId = groupId,
            name = quizTitle,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            createdUserId = userId,
        )
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.QuizInfo.NAME,
            item = quizInfo.toDTO()
        )
    }

    override suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String,
        userId: String,
    ) {
        val upLoadQuiz = quiz.copy(
            projectId = projectId,
            createdUserId = userId,
            groupId = quiz.groupId,
        )
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.Quiz.NAME,
            item = upLoadQuiz.toDTO()
        )
    }

    override suspend fun fetchQuizInfoList(
        userId: String,
    ): List<QuizInfo> {
        val res =  supabaseClientHelper.fetchListByMatchValue<QuizInfoDto>(
            tableName = SupabaseTableName.QuizInfo.NAME,
            filterCol = SupabaseColumnName.Quiz.CREATED_USER_ID,
            filterVal = userId,
            orderCol = SupabaseColumnName.UPDATED_AT,
        )
        return res.map { it.toDomain() }
    }

}