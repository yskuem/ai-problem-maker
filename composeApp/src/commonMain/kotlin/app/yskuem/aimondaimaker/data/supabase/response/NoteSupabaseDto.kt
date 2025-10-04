package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.core.serialization.KotlinInstantIso8601Serializer
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.PROJECT_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class NoteSupabaseDto(
    val id: String,
    val title: String,
    val html: String,
    @SerialName(PROJECT_ID)
    val projectId: String,
    @SerialName(CREATED_USER_ID)
    val createdUserId: String,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(CREATED_AT)
    val createdAt: Instant,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(UPDATED_AT)
    val updatedAt: Instant,
)
