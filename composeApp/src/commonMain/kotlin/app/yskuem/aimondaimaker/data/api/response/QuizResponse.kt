package app.yskuem.aimondaimaker.data.api.response

import kotlinx.serialization.Serializable


@Serializable
data class QuizResponse(
    val id: String? = null,
    val args: Args,
    val name: String
)


@Serializable
data class QuizDto(
    val answer: String,
    val category: String,
    val question: String,
    val choices: List<String>,
    val explanation: String
)


@Serializable
data class Args(
    val questions: List<QuizDto>
)
