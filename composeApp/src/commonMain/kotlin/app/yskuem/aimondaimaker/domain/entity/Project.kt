package app.yskuem.aimondaimaker.domain.entity

import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
data class Project(
    val id: String,
    val createdUserId: String,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
