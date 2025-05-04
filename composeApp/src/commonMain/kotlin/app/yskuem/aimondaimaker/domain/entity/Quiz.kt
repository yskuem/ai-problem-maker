package app.yskuem.aimondaimaker.domain.entity
import kotlinx.datetime.Instant

data class Quiz(
    val answer: String,
    val category: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,
    val projectId: String = "",
    val createdUserId: String = "",
    val createdAt: Instant,
    val updatedAt: Instant,
)
