package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun CameraPickerView(
    upLoadImage: (ByteArray) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val cameraManager = rememberCameraManager {
        coroutineScope.launch {
            val bytes = withContext(Dispatchers.Default) {
                it?.toByteArray()
            }
            bytes?.let {
                upLoadImage(it)
            }
        }
    }

    LaunchedEffect(Unit) {
        cameraManager.launch()
    }
}