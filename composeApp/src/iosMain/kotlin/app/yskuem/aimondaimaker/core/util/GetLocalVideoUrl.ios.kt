package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable

@Composable
actual fun getLocalVideoUrl(
    iOSFilename: String?,
    androidFileId: Int?,
    type: String,
): String {
    if (iOSFilename == null) {
        throw IllegalArgumentException("iOS filename must be provided for iOS platform")
    }
    return platform.Foundation.NSBundle.mainBundle
        .pathForResource(name = iOSFilename, ofType = type)!!
}
