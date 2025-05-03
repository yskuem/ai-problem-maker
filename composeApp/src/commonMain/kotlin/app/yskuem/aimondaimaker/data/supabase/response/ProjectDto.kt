package app.yskuem.aimondaimaker.data.supabase.response

import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Project.CREATED_AT
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName.Project.UPDATED_AT
import kotlinx.serialization.SerialName

data class ProjectDto(
    val id: String,
    val createdUserId: String,
    val name: String,
    @SerialName(CREATED_AT) val createdAt: String,
    @SerialName(UPDATED_AT) val updatedAt: String
)
