package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getLocalVideoUrl(
    iOSFilename: String?,
    androidFileId: Int?,
    type: String,
): String {
    if(androidFileId == null) {
        throw IllegalArgumentException("Android file ID must be provided for Android platform")
    }
    val context = LocalContext.current
    return "android.resource://${context.packageName}/${androidFileId}"
}
