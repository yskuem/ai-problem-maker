package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalWindowInfo
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
import kotlinx.cinterop.addressOf
import platform.AVFoundation.AVCapturePhotoOutput
import platform.CoreGraphics.CGRectZero
import platform.UIKit.UIColor
import platform.UIKit.UIView
import kotlinx.cinterop.cValue
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.AVFoundation.AVCaptureFlashModeOff
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.fileDataRepresentation
import platform.CoreGraphics.CGSize
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.setValue
import platform.UIKit.UIViewControllerTransitionCoordinatorProtocol
import platform.darwin.NSObject
import platform.posix.memcpy
import platform.UIKit.UIViewController


@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
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


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                    println(data.size)
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
        onImageCaptured(imageData.toByteArray())
    }

    @OptIn(ExperimentalForeignApi::class)
    fun NSData.toByteArray(): ByteArray {
        val size = this.length.toInt()
        return ByteArray(size).apply {
            usePinned { pinned ->
                // ポインタ先頭に NSData.bytes を memcpy
                memcpy(pinned.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
            }
        }
    }
}


class CameraViewController : UIViewController(nibName = null, bundle = null) {

    private lateinit var session: AVCaptureSession
    private lateinit var previewLayer: AVCaptureVideoPreviewLayer
    private lateinit var photoOutput: AVCapturePhotoOutput

    @OptIn(ExperimentalForeignApi::class)
    override fun viewDidLoad() {
        super.viewDidLoad()

        session = AVCaptureSession().apply {
            sessionPreset = AVCaptureSessionPresetPhoto
        }

        // バックカメラデバイス取得
        val backDevice = AVCaptureDevice
            .devicesWithMediaType(AVMediaTypeVideo)
            .firstOrNull { (it as AVCaptureDevice).position == AVCaptureDevicePositionBack }
                as? AVCaptureDevice ?: return

        val input = AVCaptureDeviceInput.deviceInputWithDevice(backDevice, null)
                as platform.AVFoundation.AVCaptureDeviceInput
        session.addInput(input)

        photoOutput = AVCapturePhotoOutput().apply {
            isHighResolutionCaptureEnabled()
        }
        session.addOutput(photoOutput)

        // プレビュー層セットアップ
        previewLayer = AVCaptureVideoPreviewLayer(session = session).apply {
            videoGravity = AVLayerVideoGravityResizeAspectFill
        }
        view.layer.addSublayer(previewLayer)
        session.startRunning()
    }

    // ← ここが Swift/Objective-C の override func viewWillTransition(to:with:) に相当
    @OptIn(ExperimentalForeignApi::class)
    override fun viewWillTransitionToSize(
        size: CValue<CGSize>,
        withTransitionCoordinator: UIViewControllerTransitionCoordinatorProtocol
    ) {
        super.viewWillTransitionToSize(size, withTransitionCoordinator)

        withTransitionCoordinator.animateAlongsideTransition(
            animation = { _ ->
                // CValue<CGAffineTransform> を展開して angle を計算
                val angle = withTransitionCoordinator.targetTransform.useContents {
                    -kotlin.math.atan2(this.b, this.a)
                }
                // 逆回転を適用
                previewLayer.setValue(
                    angle,
                    forKeyPath = "transform.rotation.z"
                )
            },
            completion = { _ ->
                // リセット
                previewLayer.setValue(0.0, forKeyPath = "transform.rotation.z")
                previewLayer.frame = view.bounds
            }
        )
    }
}



