package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.response.QuizApiDto
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.response.QuizInfoDto
import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class QuizRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
) : QuizRepository {
    override suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Quiz> {
        val response =
            HttpClient.postWithImage<List<QuizApiDto>>(
                imageBytes = image,
                fileName = fileName,
                extension = extension,
                path = "/generate_quizzes",
            )
        if (response.isEmpty()) {
            throw IllegalStateException("Response is empty")
        }
        return response.map { it.toDomain() }
    }


    override suspend fun saveQuizInfo(
        projectId: String,
        userId: String,
        groupId: String,
        quizTitle: String,
    ) {
        val quizInfo =
            QuizInfo(
                projectId = projectId,
                groupId = groupId,
                name = quizTitle,
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
                createdUserId = userId,
            )
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.QuizInfo.NAME,
            item = quizInfo.toDTO(),
        )
    }

    override suspend fun fetchAnsweredQuizzes(groupId: String): List<Quiz> {
        val res =
            supabaseClientHelper.fetchListByMatchValue<QuizSupabaseDto>(
                tableName = SupabaseTableName.Quiz.NAME,
                filterCol = SupabaseColumnName.Quiz.GROUP_ID,
                filterVal = groupId,
                orderCol = SupabaseColumnName.CREATED_AT,
            )
        return res.map { it.toDomain() }
    }

    override suspend fun saveQuiz(
        quiz: Quiz,
        projectId: String,
        userId: String,
    ) {
        val upLoadQuiz =
            quiz.copy(
                projectId = projectId,
                createdUserId = userId,
                groupId = quiz.groupId,
            )
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.Quiz.NAME,
            item = upLoadQuiz.toDTO(),
        )
    }

    override suspend fun fetchQuizInfoList(projectId: String): List<QuizInfo> {
        val res =
            supabaseClientHelper.fetchListByMatchValue<QuizInfoDto>(
                tableName = SupabaseTableName.QuizInfo.NAME,
                filterCol = SupabaseColumnName.PROJECT_ID,
                filterVal = projectId,
                orderCol = SupabaseColumnName.UPDATED_AT,
            )
        return res.map { it.toDomain() }
    }

    override suspend fun deleteQuizInfo(quizInfoId: String): Boolean {
        // First delete all related quizzes
        val quizzesDeleted = deleteQuizzesByQuizInfoId(quizInfoId)
        if (!quizzesDeleted) {
            return false
        }

        // Then delete the quiz info
        return supabaseClientHelper.deleteItemsByMatch(
            tableName = SupabaseTableName.QuizInfo.NAME,
            filterCol = SupabaseColumnName.Quiz.GROUP_ID,
            filterVal = quizInfoId,
        )
    }

    override suspend fun deleteQuiz(quizId: String): Boolean =
        supabaseClientHelper.deleteItemById(
            tableName = SupabaseTableName.Quiz.NAME,
            idCol = SupabaseColumnName.Quiz.ID,
            idVal = quizId,
        )

    override suspend fun deleteQuizzesByProjectId(projectId: String): Boolean =
        supabaseClientHelper.deleteItemsByMatch(
            tableName = SupabaseTableName.Quiz.NAME,
            filterCol = SupabaseColumnName.PROJECT_ID,
            filterVal = projectId,
        )

    override suspend fun deleteQuizInfosByProjectId(projectId: String): Boolean =
        supabaseClientHelper.deleteItemsByMatch(
            tableName = SupabaseTableName.QuizInfo.NAME,
            filterCol = SupabaseColumnName.PROJECT_ID,
            filterVal = projectId,
        )

    override suspend fun deleteQuizzesByQuizInfoId(quizInfoId: String): Boolean =
        supabaseClientHelper.deleteItemsByMatch(
            tableName = SupabaseTableName.Quiz.NAME,
            filterCol = SupabaseColumnName.Quiz.GROUP_ID,
            filterVal = quizInfoId,
        )
}
