package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.extension.toDTO
import app.yskuem.aimondaimaker.data.extension.toDomain
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ProjectRepositoryImpl(
    private val authRepository: AuthRepository,
    private val supabaseClientHelper: SupabaseClientHelper,
    private val quizRepository: QuizRepository,
    private val noteRepository: NoteRepository,
) : ProjectRepository {
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addProject(projectName: String): Project {
        val project =
            Project(
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
        val res =
            supabaseClientHelper.fetchListByMatchValue<ProjectDto>(
                tableName = SupabaseTableName.Project.NAME,
                filterCol = SupabaseColumnName.Project.CREATE_USER_ID,
                filterVal = authRepository.getUserId(),
                orderCol = SupabaseColumnName.CREATED_AT,
            )
        return res.map { it.toDomain() }
    }

    override suspend fun updateProject(targetProject: Project): Project? {
        val res =
            supabaseClientHelper.updateItemById<ProjectDto>(
                tableName = SupabaseTableName.Project.NAME,
                idCol = SupabaseColumnName.Project.ID,
                idVal = targetProject.id,
                changes = targetProject.toDTO(),
            )
        return res?.toDomain()
    }

    override suspend fun deleteProject(projectId: String): Boolean =
        try {
            // 削除順序: quiz → note → quizinfo → project
            val quizDeleted = quizRepository.deleteQuizzesByProjectId(projectId)
            val noteDeleted = noteRepository.deleteNotesByProjectId(projectId)
            val quizInfoDeleted = quizRepository.deleteQuizInfosByProjectId(projectId)
            val projectDeleted =
                supabaseClientHelper.deleteItemById(
                    tableName = SupabaseTableName.Project.NAME,
                    idCol = SupabaseColumnName.Project.ID,
                    idVal = projectId,
                )

            // すべての削除が成功した場合のみtrueを返す
            quizDeleted && noteDeleted && quizInfoDeleted && projectDeleted
        } catch (e: Exception) {
            false
        }
}
