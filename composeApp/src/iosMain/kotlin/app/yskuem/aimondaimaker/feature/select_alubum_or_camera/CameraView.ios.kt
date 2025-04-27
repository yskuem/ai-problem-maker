package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView() {
    // セッションとデバイスのセットアップ
    val session = AVCaptureSession().apply {
        sessionPreset = AVCaptureSessionPresetPhoto
    }

    val backDevice = AVCaptureDevice
        .devicesWithMediaType(AVMediaTypeVideo)
        .firstOrNull { (it as AVCaptureDevice).position == AVCaptureDevicePositionBack }
            as? AVCaptureDevice
        ?: return

    val input = AVCaptureDeviceInput.deviceInputWithDevice(backDevice, null) as AVCaptureDeviceInput
    session.addInput(input)

    // Deprecated: AVCaptureStillImageOutput -> Use AVCapturePhotoOutput
    val photoOutput = AVCapturePhotoOutput()
    session.addOutput(photoOutput)

    // プレビュー用レイヤー
    val previewLayer = remember {
        AVCaptureVideoPreviewLayer(session = session).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
    }

    // カスタム UIView
    class PreviewView(frame: CValue<CGRect>) : UIView(frame) {
        override fun layoutSubviews() {
            super.layoutSubviews()
            previewLayer.frame = bounds
        }
    }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            // CGRectZero を CValue<CGRect> にラップ
            PreviewView(cValue { CGRectZero }).apply {
                backgroundColor = UIColor.blackColor
                layer.addSublayer(previewLayer)
                session.startRunning()
            }
        },
        update = { /* layoutSubviews() で自動調整 */ }
    )
}
