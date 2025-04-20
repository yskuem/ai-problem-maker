package app.yskuem.aimondaimaker.feature.main.viewmodel

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val imagePicker: ImagePicker,
    private val problemRepository: ProblemRepository,
): ScreenModel {

    private val _state = MutableStateFlow(false)
    val state = _state.asStateFlow()

    fun onSelectImage() {
        screenModelScope.launch {
            val image = imagePicker.pickImage() ?: return@launch
            val problems = problemRepository.fetchFromImage(
                image = image.readBytes(),
                fileName = image.nameWithoutExtension,
                extension = image.nameWithoutExtension,
            )
            println(problems)
        }
    }
}