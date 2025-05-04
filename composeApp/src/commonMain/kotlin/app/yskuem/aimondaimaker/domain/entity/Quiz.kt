package app.yskuem.aimondaimaker.domain.entity
import kotlinx.datetime.Instant

data class Quiz(
    val id: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,
    val projectId: String = "",
    val createdUserId: String = "",
    val groupId: String,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
