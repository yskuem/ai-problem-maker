package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.response.SharedQuizDto
import app.yskuem.aimondaimaker.domain.data.repository.SharedQuizRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.SharedQuiz
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SharedQuizRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
) : SharedQuizRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun saveSharedQuiz(
        groupId: String,
        quizData: List<Quiz>,
        userId: String,
    ): SharedQuiz {
        val sharedQuiz = SharedQuiz(
            id = Uuid.random().toString(),
            groupId = groupId,
            createdUserId = userId,
            quizData = quizData,
            createdAt = Clock.System.now(),
        )

        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.SharedQuiz.NAME,
            item = sharedQuiz.toDTO(),
        )

        return sharedQuiz
    }

    override suspend fun getSharedQuiz(groupId: String): SharedQuiz? {
        val results = supabaseClientHelper.fetchListByMatchValue<SharedQuizDto>(
            tableName = SupabaseTableName.SharedQuiz.NAME,
            filterCol = SupabaseColumnName.SharedQuiz.GROUP_ID,
            filterVal = groupId,
            orderCol = SupabaseColumnName.CREATED_AT,
        )
        return results.firstOrNull()?.toDomain()
    }
}