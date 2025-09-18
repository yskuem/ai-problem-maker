package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.core.serialization.KotlinInstantIso8601Serializer
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.PROJECT_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.CREATED_USER_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Quiz.GROUP_ID
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.UPDATED_AT
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalTime::class)
@Serializable
data class QuizInfoDto(
    @SerialName(PROJECT_ID)
    val projectId: String,
    @SerialName(GROUP_ID)
    val groupId: String,
    @SerialName(CREATED_USER_ID)
    val createdUserId: String,
    val name: String,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(UPDATED_AT)
    val updatedAt: Instant,
    @Serializable(with = KotlinInstantIso8601Serializer::class)
    @SerialName(CREATED_AT)
    val createdAt: Instant,
)
