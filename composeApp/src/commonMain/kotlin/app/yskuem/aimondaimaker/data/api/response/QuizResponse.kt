package app.yskuem.aimondaimaker.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class QuizResponse(
    val id: String? = null,
    val args: Args,
    val name: String
)


@Serializable
data class QuizDto(
    val id: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,
    val title: String,
    @SerialName("group_id")
    val groupId: String,
)


@Serializable
data class Args(
    val questions: List<QuizDto>,
)
