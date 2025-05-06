package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Project.CREATE_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectDto(
    val id: String,

    @SerialName(CREATE_USER_ID)
    val createdUserId: String,

    val name: String,

    @SerialName(CREATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,

    @SerialName(UPDATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant
)
