package app.yskuem.aimondaimaker.domain.entity

data class Problem(
    val answer: String,
    val category: String,
    val question: String,
    val choices: List<String>,
    val explanation: String
)
