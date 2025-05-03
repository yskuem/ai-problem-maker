package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.extension.toDTO
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectRepositoryImpl(
    private val authRepository: AuthRepository,
    private val supabaseClientHelper: SupabaseClientHelper,
) : ProjectRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addProject(projectName: String) {
        val userId = authRepository.getUserId()
        if(userId == null) {
            throw IllegalStateException("User ID is null. Please sign in first.")
        }
        val project = Project(
            id = Uuid.random().toString(),
            createdUserId = userId,
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now(),
            name = projectName,
        )
        supabaseClientHelper.addItem<ProjectDto>(
            tableName = SupabaseTableName.Project.NAME,
            item = project.toDTO(),
        )
    }

    override suspend fun fetchProjectList(): List<Project> {
        TODO("Not yet implemented")
    }

}