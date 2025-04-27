package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
actual fun CameraView() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // カメラプロバイダーとプレビュー用のPreviewViewを用意
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    // ImageCaptureユースケースの作成
    val imageCapture = remember { ImageCapture.Builder().build() }

    // カメラの初期化とユースケースのバインド
    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().apply {
            surfaceProvider = previewView.surfaceProvider
        }
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        try {
            // 既存のユースケースをすべて解除してから新たにバインド
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraView", "カメラの初期化に失敗しました", exc)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // カメラプレビューの表示
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
        // 撮影ボタンの配置
        Button(
            onClick = {
                // MediaStore APIを利用してDownloadsフォルダに保存するためのContentValuesを設定
                val filename = "JPEG_${System.currentTimeMillis()}.jpg"
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }
                // MediaStoreのDownloadsコレクションに対して出力先を指定
                val outputOptions = ImageCapture.OutputFileOptions.Builder(
                    context.contentResolver,
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                    contentValues
                ).build()

                println(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY))

                // 画像の撮影と保存処理
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            // savedUriがnullの場合、生成したURIを補完
                            val savedUri = outputFileResults.savedUri
                                ?: Uri.withAppendedPath(MediaStore.Downloads.EXTERNAL_CONTENT_URI, filename)
                        }
                        override fun onError(exception: ImageCaptureException) {
                            onError(exception)
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("撮影")
        }
    }
}