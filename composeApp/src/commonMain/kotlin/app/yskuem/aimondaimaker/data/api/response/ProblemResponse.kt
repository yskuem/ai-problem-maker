package app.yskuem.aimondaimaker.data.api.response

import app.yskuem.aimondaimaker.domain.entity.Problem
import kotlinx.serialization.Serializable


@Serializable
data class ProblemResponse(
    val id: String? = null,
    val args: Args,
    val name: String
)


@Serializable
data class ProblemDto(
    val answer: String,
    val category: String,
    val question: String,
    val choices: List<String>,
    val explanation: String
)


@Serializable
data class Args(
    val questions: List<ProblemDto>
)
