package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.position
import platform.CoreGraphics.CGRect
import androidx.compose.ui.viewinterop.UIKitView
import platform.AVFoundation.AVCapturePhotoOutput
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIColor
import platform.UIKit.UIView
import kotlinx.cinterop.cValue
import platform.AVFoundation.AVCaptureFlashModeOff
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.fileDataRepresentation
import platform.Foundation.NSError
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView() {
    val session = AVCaptureSession().apply {
        sessionPreset = AVCaptureSessionPresetPhoto
    }
    val backDevice = AVCaptureDevice
        .devicesWithMediaType(AVMediaTypeVideo)
        .firstOrNull { (it as AVCaptureDevice).position == AVCaptureDevicePositionBack }
            as? AVCaptureDevice ?: return

    val input = AVCaptureDeviceInput.deviceInputWithDevice(backDevice, null) as AVCaptureDeviceInput
    session.addInput(input)

    val photoOutput = AVCapturePhotoOutput().apply {
        isHighResolutionCaptureEnabled()
    }
    session.addOutput(photoOutput)

    val previewLayer = remember {
        AVCaptureVideoPreviewLayer(session = session).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
    }

    class PreviewView(frame: CValue<CGRect>) : UIView(frame) {
        override fun layoutSubviews() {
            super.layoutSubviews()
            previewLayer.frame = bounds
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        UIKitView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(cValue { CGRectZero }).apply {
                    backgroundColor = UIColor.blackColor
                    layer.addSublayer(previewLayer)
                    session.startRunning()
                }
            }
        )

        Button(
            onClick = {
                val settings = AVCapturePhotoSettings.photoSettings().apply {
                    flashMode = AVCaptureFlashModeOff
                    isHighResolutionPhotoEnabled()
                    isAutoStillImageStabilizationEnabled()
                }
                photoOutput.capturePhotoWithSettings(settings, PhotoCaptureDelegate { data ->
                    // 画像データ利用処理
                })
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text("撮影")
        }
    }
}



// キャプチャコールバックを受け取るデリゲート
class PhotoCaptureDelegate(
    private val onImageCaptured: (ByteArray) -> Unit
) : NSObject(), AVCapturePhotoCaptureDelegateProtocol {

    // キャプチャ完了時
    @OptIn(ExperimentalForeignApi::class)
    override fun captureOutput(
        output: AVCapturePhotoOutput,
        didFinishProcessingPhoto: AVCapturePhoto,
        error: NSError?
    ) {
        error?.let {
            println("Photo capture error: ${it.localizedDescription}")
            return
        }
        // 画像データを JPEG として取得
        val imageData = didFinishProcessingPhoto.fileDataRepresentation() ?: return
    }
}


