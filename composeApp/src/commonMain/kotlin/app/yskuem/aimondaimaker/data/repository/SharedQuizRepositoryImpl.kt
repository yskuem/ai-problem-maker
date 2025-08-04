package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.extension.toSharedQuiz
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.response.SharedQuizDto
import app.yskuem.aimondaimaker.domain.data.repository.SharedQuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SharedQuizRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
) : SharedQuizRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveSharedQuizzes(
        groupId: String,
        quizData: List<Quiz>,
        userId: String,
    ): List<Quiz> {
        val savedQuizzes = mutableListOf<Quiz>()
        
        quizData.forEach { quiz ->
            val sharedQuiz = quiz.toSharedQuiz(
                sharedQuizId = Uuid.random().toString()
            )

            supabaseClientHelper.addItem(
                tableName = SupabaseTableName.SharedQuiz.NAME,
                item = sharedQuiz.toDTO(),
            )
            
            savedQuizzes.add(quiz)
        }

        return savedQuizzes
    }

    override suspend fun getSharedQuizzes(groupId: String): List<Quiz> {
        val results = supabaseClientHelper.fetchListByMatchValue<SharedQuizDto>(
            tableName = SupabaseTableName.SharedQuiz.NAME,
            filterCol = SupabaseColumnName.SharedQuiz.GROUP_ID,
            filterVal = groupId,
            orderCol = SupabaseColumnName.CREATED_AT,
        )
        
        // Convert SharedQuiz back to Quiz entities
        return results.map { sharedQuizDto ->
            val sharedQuiz = sharedQuizDto.toDomain()
            Quiz(
                id = sharedQuiz.id,
                answer = sharedQuiz.answer,
                question = sharedQuiz.question,
                choices = sharedQuiz.choices,
                explanation = sharedQuiz.explanation,
                projectId = "", // Not stored in shared_quiz table
                createdUserId = sharedQuiz.createdUserId,
                groupId = sharedQuiz.groupId,
                title = sharedQuiz.title,
                createdAt = sharedQuiz.createdAt,
                updatedAt = sharedQuiz.updatedAt,
            )
        }
    }
}