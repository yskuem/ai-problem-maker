package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.extension.toDTO
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
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
    override suspend fun addProject(projectName: String): Project {

        val project = Project(
            id = Uuid.random().toString(),
            createdUserId = authRepository.getUserId(),
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now(),
            name = projectName,
        )
        supabaseClientHelper.addItem<ProjectDto>(
            tableName = SupabaseTableName.Project.NAME,
            item = project.toDTO(),
        )
        return project
    }

    override suspend fun fetchProjectList(): List<Project> {

        val res = supabaseClientHelper.fetchListByMatchValue<ProjectDto>(
            tableName = SupabaseTableName.Project.NAME,
            filterCol = SupabaseColumnName.Project.CREATE_USER_ID,
            filterVal = authRepository.getUserId(),
            orderCol = SupabaseColumnName.Project.CREATED_AT,
        )
        return res.map { it.toDomain() }
    }

}