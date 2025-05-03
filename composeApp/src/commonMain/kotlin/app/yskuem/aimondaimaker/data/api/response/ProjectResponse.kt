package app.yskuem.aimondaimaker.data.api.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Project.CREATED_AT
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
@Serializable
data class ProjectResponse(
    val id: String,
    @SerialName(CREATED_AT) val createdAt: String
)