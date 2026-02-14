package app.yskuem.aimondaimaker.feature.select_project.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.SubscriptionRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectProjectScreenViewModel(
    private val projectRepository: ProjectRepository,
    private val subscriptionRepository: SubscriptionRepository,
) : ScreenModel {
    companion object {
        private const val PAGE_SIZE = 20
    }

    private val _projects = MutableStateFlow<DataUiState<List<Project>>>(DataUiState.Loading)
    val projects = _projects.asStateFlow()

    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore = _isLoadingMore.asStateFlow()

    private val _hasMoreData = MutableStateFlow(true)
    val hasMoreData = _hasMoreData.asStateFlow()

    private var currentOffset = 0

    init {
        screenModelScope.launch {
            subscriptionRepository.isSubscribed().collect {
                _isSubscribed.value = it
            }
        }
    }

    private fun onFetchProjectList() {
        screenModelScope.launch {
            currentOffset = 0
            _hasMoreData.value = true
            val result =
                runCatching {
                    projectRepository.fetchProjectList(
                        limit = PAGE_SIZE,
                        offset = 0,
                    )
                }
            result
                .onSuccess {
                    _projects.value = DataUiState.Success(it)
                    currentOffset = it.size
                    _hasMoreData.value = it.size >= PAGE_SIZE
                }.onFailure {
                    _projects.value = DataUiState.Error(it)
                }
        }
    }

    fun fetchMoreProjects() {
        if (_isLoadingMore.value || !_hasMoreData.value) return
        val currentState = _projects.value
        if (currentState !is DataUiState.Success) return

        screenModelScope.launch {
            _isLoadingMore.value = true
            val result =
                runCatching {
                    projectRepository.fetchProjectList(
                        limit = PAGE_SIZE,
                        offset = currentOffset,
                    )
                }
            result
                .onSuccess { newProjects ->
                    val allProjects = currentState.data + newProjects
                    _projects.value = DataUiState.Success(allProjects)
                    currentOffset += newProjects.size
                    _hasMoreData.value = newProjects.size >= PAGE_SIZE
                }.onFailure {
                    println(it)
                }
            _isLoadingMore.value = false
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

    fun deleteProject(
        projectId: String,
        currentProjects: List<Project>,
    ) {
        screenModelScope.launch {
            val result =
                runCatching {
                    projectRepository.deleteProject(projectId)
                }
            result
                .onSuccess { success ->
                    if (success) {
                        val updatedProjects = currentProjects.filter { it.id != projectId }
                        _projects.value = DataUiState.Success(updatedProjects)
                    }
                }.onFailure {
                    println(it)
                }
        }
    }
}
