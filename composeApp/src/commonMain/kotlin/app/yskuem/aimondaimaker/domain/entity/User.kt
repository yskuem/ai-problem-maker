package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
