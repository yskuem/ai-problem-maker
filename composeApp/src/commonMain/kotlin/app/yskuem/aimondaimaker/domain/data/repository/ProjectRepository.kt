package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Project

interface ProjectRepository {
    suspend fun addProject(projectName: String)
    suspend fun fetchProjectList(): List<Project>
}