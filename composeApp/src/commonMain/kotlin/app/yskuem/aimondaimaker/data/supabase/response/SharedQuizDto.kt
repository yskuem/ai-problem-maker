package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.GROUP_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.QUIZ_DATA
import app.yskuem.aimondaimaker.domain.entity.Quiz
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SharedQuizDto(
    @SerialName(ID)
    val id: String,
    @SerialName(GROUP_ID)
    val groupId: String,
    @SerialName(CREATED_USER_ID)
    val createdUserId: String,
    @SerialName(QUIZ_DATA)
    val quizData: List<Quiz>,
    @SerialName(CREATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
)