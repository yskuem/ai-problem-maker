package app.yskuem.aimondaimaker.data.api.response

import kotlinx.serialization.Serializable

@Serializable
data class NoteApiDto(
    val id: String,
    val title: String,
    val html: String,
)