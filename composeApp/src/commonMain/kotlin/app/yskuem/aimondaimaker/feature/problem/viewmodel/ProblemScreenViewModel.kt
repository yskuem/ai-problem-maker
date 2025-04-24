package app.yskuem.aimondaimaker.feature.problem.viewmodel

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository
import app.yskuem.aimondaimaker.domain.entity.Problem
import app.yskuem.aimondaimaker.feature.problem.uiState.ProblemUiState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProblemScreenViewModel(
    private val imagePicker: ImagePicker,
    private val problemRepository: ProblemRepository,
) : ScreenModel {

    private val _isLoading = MutableStateFlow(false)
    private val _hasError  = MutableStateFlow(false)
    private val _problems = MutableStateFlow<List<Problem>>(emptyList())


    val uiState: StateFlow<ProblemUiState> = combine(
        _isLoading, _hasError,_problems
    ) { isLoading, hasError, problems ->
        ProblemUiState(
            isLoading = isLoading,
            hasError = hasError,
            problems = problems,
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProblemUiState()
    )

    fun onSelectImage() {
        screenModelScope.launch {
            val image = imagePicker.pickImage() ?: return@launch
            _isLoading.value = true
            try {
                val problems = problemRepository.fetchFromImage(
                    image = image.readBytes(),
                    fileName = image.nameWithoutExtension,
                    extension = image.extension,
                )
                _problems.value = problems
            } catch (e: Exception) {
                _hasError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}
