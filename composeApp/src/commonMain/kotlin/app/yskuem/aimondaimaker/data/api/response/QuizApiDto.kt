package app.yskuem.aimondaimaker.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizApiDto(
    val id: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,
    val title: String,
    @SerialName("group_id")
    val groupId: String,
)
