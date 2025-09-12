package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Project.CREATE_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.InstantComponentSerializer
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class ProjectDto(
    val id: String,
    @SerialName(CREATE_USER_ID)
    val createdUserId: String,
    val name: String,
    @Serializable(with = InstantComponentSerializer::class)
    @SerialName(CREATED_AT)
    val createdAt: Instant,
    @Serializable(with = InstantComponentSerializer::class)
    @SerialName(UPDATED_AT)
    val updatedAt: Instant,
)
