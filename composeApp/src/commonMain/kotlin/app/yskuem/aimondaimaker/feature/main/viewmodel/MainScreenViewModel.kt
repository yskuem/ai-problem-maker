package app.yskuem.aimondaimaker.feature.main.viewmodel

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val imagePicker: ImagePicker,
): ScreenModel {

    private val _state = MutableStateFlow(false)
    val state = _state.asStateFlow()

    fun onSelectImage() {
        screenModelScope.launch {
            imagePicker.pickImage()
        }
    }
}