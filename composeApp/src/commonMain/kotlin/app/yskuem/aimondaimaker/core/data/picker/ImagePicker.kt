package app.yskuem.aimondaimaker.core.data.picker

import io.github.vinceglb.filekit.PlatformFile

interface ImagePicker {
    suspend fun pickImage(): PlatformFile?
}
