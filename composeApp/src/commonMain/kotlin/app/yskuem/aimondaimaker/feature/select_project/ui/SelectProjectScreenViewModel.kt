package app.yskuem.aimondaimaker.feature.select_project.ui

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch

class SelectProjectScreenViewModel(
    private val projectRepository: ProjectRepository,
) : ScreenModel {
    init {
        onFetchProjectList()
    }

    fun onFetchProjectList() {
        screenModelScope.launch {
            val result = runCatching {
                projectRepository.fetchProjectList()
            }
            result.onSuccess {
                println(it)
            }.onFailure {
                println("Failed to fetch project list: ${it.message}")
            }
        }
    }
}