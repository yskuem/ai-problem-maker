package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.GROUP_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizInfoDto(
    @SerialName(GROUP_ID)
    val groupId: String,

    @SerialName(CREATED_USER_ID)
    val createdUserId: String,

    val name: String,

    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName(UPDATED_AT)
    val updatedAt: Instant,

    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName(CREATED_AT)
    val createdAt: Instant,
)
