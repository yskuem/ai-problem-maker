package app.yskuem.aimondaimaker.data.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PdfRequestDto(
    val title: String,
    val questions: List<QuestionDto>,
    @SerialName("color_mode") val colorMode: Boolean = true,
)

@Serializable
data class QuestionDto(
    val question: String,
    val choices: List<String>,
    val answer: String,
    val explanation: String,
)
