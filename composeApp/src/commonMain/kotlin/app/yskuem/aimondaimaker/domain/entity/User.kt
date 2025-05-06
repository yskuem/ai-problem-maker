package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class User(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun initialState(id: String): User = User(
            id = id,
            name = "",
            avatarUrl = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )
    }
}
