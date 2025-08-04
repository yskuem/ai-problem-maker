package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.ANSWER
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.CHOICES
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.EXPLANATION
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.GROUP_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.QUESTION
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.SharedQuiz.TITLE
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
    @SerialName(ANSWER)
    val answer: String,
    @SerialName(QUESTION)
    val question: String,
    @SerialName(CHOICES)
    val choices: List<String>,
    @SerialName(EXPLANATION)
    val explanation: String,
    @SerialName(TITLE)
    val title: String,
    @SerialName(CREATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
    @SerialName(UPDATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant,
)