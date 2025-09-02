package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
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
            // Create a new Quiz with a unique ID for the shared_quiz table
            val sharedQuiz =
                quiz.copy(
                    id = Uuid.random().toString(),
                    createdUserId = userId,
                    groupId = groupId,
                )

            supabaseClientHelper.addItem(
                tableName = SupabaseTableName.SharedQuiz.NAME,
                item = sharedQuiz.toDTO(),
            )

            savedQuizzes.add(sharedQuiz)
        }

        return savedQuizzes
    }

    override suspend fun getSharedQuizzes(groupId: String): List<Quiz> {
        val results =
            supabaseClientHelper.fetchListByMatchValue<QuizSupabaseDto>(
                tableName = SupabaseTableName.SharedQuiz.NAME,
                filterCol = SupabaseColumnName.SharedQuiz.GROUP_ID,
                filterVal = groupId,
                orderCol = SupabaseColumnName.CREATED_AT,
            )

        // Convert QuizSupabaseDto directly to Quiz entities
        return results.map { quizDto -> quizDto.toDomain() }
    }
}
