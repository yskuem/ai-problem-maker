package app.yskuem.aimondaimaker.feature.select_project.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectProjectScreenViewModel(
    private val projectRepository: ProjectRepository,
    adRepository: AdRepository,
) : ScreenModel {
    private val _projects = MutableStateFlow<DataUiState<List<Project>>>(DataUiState.Loading)
    val projects = _projects.asStateFlow()

    private fun onFetchProjectList() {
        screenModelScope.launch {
            val result =
                runCatching {
                    projectRepository.fetchProjectList()
                }
            result
                .onSuccess {
                    _projects.value = DataUiState.Success(it)
                }.onFailure {
                    _projects.value = DataUiState.Error(it)
                }
        }
    }

    fun refreshProjectList() {
        _projects.value = DataUiState.Loading
        onFetchProjectList()
    }

    fun editProject(
        targetProject: Project,
        currentProjects: List<Project>,
    ) {
        screenModelScope.launch {
            val result =
                runCatching {
                    projectRepository.updateProject(targetProject)
                }
            result
                .onSuccess {
                    val updateProjects =
                        currentProjects.map {
                            if (it.id == targetProject.id) targetProject else it
                        }
                    _projects.value = DataUiState.Success(updateProjects)
                }.onFailure {
                    println(it)
                }
        }
    }
}
