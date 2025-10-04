package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.core.serialization.KotlinInstantIso8601Serializer
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.User.AVATAR_URL
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class UserDto(
    val id: String,
    @SerialName(AVATAR_URL)
    val avatarUrl: String,
    val name: String,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(CREATED_AT)
    val createdAt: Instant,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(UPDATED_AT)
    val updatedAt: Instant,
)
