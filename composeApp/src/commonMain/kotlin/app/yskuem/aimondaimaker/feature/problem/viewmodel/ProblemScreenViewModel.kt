package app.yskuem.aimondaimaker.feature.problem.viewmodel

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository
import app.yskuem.aimondaimaker.domain.entity.Problem
import app.yskuem.aimondaimaker.feature.problem.uiState.ProblemUiState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShowProblemScreenViewModel(
    private val problemRepository: ProblemRepository,
) : ScreenModel {

    private val _problems = MutableStateFlow<DataUiState<List<Problem>>>(DataUiState.Loading)
    private val _currentProblemIndex = MutableStateFlow(0)


    val uiState: StateFlow<ProblemUiState> = combine(
        _problems, _currentProblemIndex,
    ) { problems, currentProblemIndex ->
        ProblemUiState(
            problems = problems,
            currentProblemIndex = currentProblemIndex
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProblemUiState()
    )

    fun onFetchProblems(
        imageByte: ByteArray,
        fileName: String,
        extension: String
    ) {
        screenModelScope.launch {
            _problems.value = DataUiState.Loading
            try {
                val problems = problemRepository.fetchFromImage(
                    image = imageByte,
                    fileName = fileName,
                    extension = extension,
                )
                _problems.value = DataUiState.Success(problems)
            } catch (e: Exception) {
                _problems.value = DataUiState.Error(e)
            }
        }
    }
}
