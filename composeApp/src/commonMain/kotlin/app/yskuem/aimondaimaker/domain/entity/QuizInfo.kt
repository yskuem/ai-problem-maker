package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class QuizInfo(
    val groupId: String,
    val createdUserId: String,
    val name: String,
    val updatedAt: Instant,
    val createdAt: Instant,
)
