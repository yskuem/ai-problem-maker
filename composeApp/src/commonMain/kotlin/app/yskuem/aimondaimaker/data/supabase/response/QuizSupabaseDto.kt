package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.PROJECT_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.GROUP_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT

@Serializable
data class QuizSupabaseDto(
    val id: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,

    @SerialName(PROJECT_ID)
    val projectId: String,

    @SerialName(CREATED_USER_ID)
    val createdUserId: String,

    @SerialName(GROUP_ID)
    val groupId: String,

    val title: String,

    @SerialName(CREATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,

    @SerialName(UPDATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant,
)
