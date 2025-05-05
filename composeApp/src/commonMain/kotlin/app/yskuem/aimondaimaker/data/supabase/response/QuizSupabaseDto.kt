package app.yskuem.aimondaimaker.data.supabase.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class QuizSupabaseDto(
    val id: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,

    @SerialName("project_id")
    val projectId: String,

    @SerialName("created_user_id")
    val createdUserId: String,

    @SerialName("group_id")
    val groupId: String,

    val title: String,

    @SerialName("created_at")
    val createdAt: Instant,

    @SerialName("updated_at")
    val updatedAt: Instant,
)
