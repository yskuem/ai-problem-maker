package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class SharedQuiz(
    val id: String,
    val groupId: String,
    val createdUserId: String,
    val quizData: List<Quiz>,
    val createdAt: Instant,
)