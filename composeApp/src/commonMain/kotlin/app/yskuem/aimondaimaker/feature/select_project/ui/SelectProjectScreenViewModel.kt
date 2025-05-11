package app.yskuem.aimondaimaker.feature.select_project.ui

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.ui.ShowAnsweredQuizzesScreen
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectProjectScreenViewModel(
    private val projectRepository: ProjectRepository,
) : ScreenModel {
    init {
        onFetchProjectList()
    }

    private val _projects = MutableStateFlow<DataUiState<List<Project>>>(DataUiState.Loading)
    val projects = _projects.asStateFlow()


    private fun onFetchProjectList() {
        screenModelScope.launch {
            val result = runCatching {
                projectRepository.fetchProjectList()
            }
            result.onSuccess {
                _projects.value = DataUiState.Success(it)
            }.onFailure {
                _projects.value = DataUiState.Error(it)
            }
        }
    }
}