package app.yskuem.aimondaimaker.data.picker

import app.yskuem.aimondaimaker.core.data.picker.ImagePicker
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFilePicker

class ImagePickerImpl : ImagePicker {
    override suspend fun pickImage(): PlatformFile? = FileKit.openFilePicker()
}
