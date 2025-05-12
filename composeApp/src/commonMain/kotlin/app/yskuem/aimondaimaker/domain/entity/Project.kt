package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class Project(
    val id: String,
    val createdUserId: String,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
