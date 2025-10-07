package app.yskuem.aimondaimaker.domain.entity

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class Note(
    val id: String,
    val title: String,
    val html: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
