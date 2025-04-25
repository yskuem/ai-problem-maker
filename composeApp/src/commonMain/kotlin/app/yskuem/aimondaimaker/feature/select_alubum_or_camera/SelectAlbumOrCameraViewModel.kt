package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import app.yskuem.aimondaimaker.feature.problem.ui.ShowProblemScreen
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.navigator.Navigator
import io.github.vinceglb.filekit.PlatformFile

class SelectAlbumOrCameraViewModel(
    private val imagePicker: ImagePicker,
): ScreenModel {

    suspend fun onSelectAlbum(
        onNavigateNextPage: (PlatformFile) -> Unit
    ) {
        val image = imagePicker.pickImage()
        if(image != null) {
            onNavigateNextPage(image)
        }
    }
}