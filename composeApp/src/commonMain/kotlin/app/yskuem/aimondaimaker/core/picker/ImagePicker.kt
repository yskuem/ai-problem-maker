package app.yskuem.aimondaimaker.core.picker

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFilePicker

interface ImagePicker {
    suspend fun pickImage(): PlatformFile?
}
