package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import cafe.adriel.voyager.core.model.ScreenModel
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.nameWithoutExtension
import io.github.vinceglb.filekit.readBytes

class SelectAlbumOrCameraViewModel(
    private val imagePicker: ImagePicker,
): ScreenModel {

    suspend fun onSelectAlbum(
        onNavigateNextPage: (ByteArray,String,String) -> Unit
    ) {
        val image = imagePicker.pickImage()
        if(image != null) {
            onNavigateNextPage(
                image.readBytes(),
                image.nameWithoutExtension,
                image.extension
            )
        }
    }
}