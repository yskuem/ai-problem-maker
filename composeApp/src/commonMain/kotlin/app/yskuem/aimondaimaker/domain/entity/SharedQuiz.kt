package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class SharedQuiz(
    val id: String,
    val groupId: String,
    val createdUserId: String,
    val answer: String,
    val question: String,
    val choices: List<String>,
    val explanation: String,
    val title: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)