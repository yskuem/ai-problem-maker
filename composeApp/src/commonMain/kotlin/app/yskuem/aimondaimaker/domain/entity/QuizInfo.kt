package app.yskuem.aimondaimaker.domain.entity

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class QuizInfo(
    val projectId: String,
    val groupId: String,
    val createdUserId: String,
    val name: String,
    val updatedAt: Instant,
    val createdAt: Instant,
)
