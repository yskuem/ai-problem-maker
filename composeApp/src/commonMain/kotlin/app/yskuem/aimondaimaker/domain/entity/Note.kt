package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant

data class Note(
    val id: String,
    val title: String,
    val html: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
