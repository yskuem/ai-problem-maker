package app.yskuem.aimondaimaker.core.picker

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFilePicker

class ImagePickerImpl : ImagePicker {
    override suspend fun pickImage(): PlatformFile? {
        return FileKit.openFilePicker()
    }
}