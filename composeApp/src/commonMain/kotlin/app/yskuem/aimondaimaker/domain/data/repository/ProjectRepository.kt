package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Project

interface ProjectRepository {
    suspend fun addProject(projectName: String): Project

    suspend fun fetchProjectList(): List<Project>

    suspend fun updateProject(targetProject: Project): Project?

    suspend fun deleteProject(projectId: String): Boolean
}
