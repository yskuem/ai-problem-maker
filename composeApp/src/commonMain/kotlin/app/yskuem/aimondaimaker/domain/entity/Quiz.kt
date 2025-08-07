package app.yskuem.aimondaimaker.domain.entity
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

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
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant,
)
