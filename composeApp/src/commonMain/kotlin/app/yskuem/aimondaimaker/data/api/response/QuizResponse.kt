package app.yskuem.aimondaimaker.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class QuizResponse(
    val id: String? = null,
    @SerialName("group_id")
    val groupId: String,
    val args: Args,
    val name: String
)


@Serializable
data class QuizDto(
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String
)


@Serializable
data class Args(
    val questions: List<QuizDto>,
    val title: String,
)
