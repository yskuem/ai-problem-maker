package app.yskuem.aimondaimaker.core.picker

import io.github.vinceglb.filekit.PlatformFile

interface ImagePicker {
    suspend fun pickImage(): PlatformFile?
}
