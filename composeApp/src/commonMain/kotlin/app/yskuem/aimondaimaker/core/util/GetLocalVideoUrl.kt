package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable

@Composable
expect fun getLocalVideoUrl(
    iOSFilename: String? = null,
    androidFileId: Int? = null,
    type: String = "mp4",
): String