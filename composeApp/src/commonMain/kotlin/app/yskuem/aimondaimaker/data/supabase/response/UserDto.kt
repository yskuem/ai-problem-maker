package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.User.AVATAR_URL
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String,
    @SerialName(AVATAR_URL)
    val avatarUrl: String,
    val name: String,
    @SerialName(CREATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
    @SerialName(UPDATED_AT)
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant,
)
