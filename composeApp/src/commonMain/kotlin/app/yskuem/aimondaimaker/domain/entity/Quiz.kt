package app.yskuem.aimondaimaker.domain.entity
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
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
