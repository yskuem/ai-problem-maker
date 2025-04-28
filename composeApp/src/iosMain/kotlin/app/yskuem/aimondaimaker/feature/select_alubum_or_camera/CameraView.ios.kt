package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import kotlinx.cinterop.usePinned
import platform.AVFoundation.AVCaptureFlashModeOff
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.fileDataRepresentation
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.darwin.NSObject
import platform.posix.memcpy


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

        ShutterButton (
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ){
            val settings = AVCapturePhotoSettings.photoSettings().apply {
                flashMode = AVCaptureFlashModeOff
                isHighResolutionPhotoEnabled()
                isAutoStillImageStabilizationEnabled()
            }
            photoOutput.capturePhotoWithSettings(settings, PhotoCaptureDelegate { data ->
                println(data.size)
            })
        }
    }
}


@Composable
fun ShutterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // ボタンの状態
    var pressed by remember { mutableStateOf(false) }

    // アニメーション設定
    val animatedScale by animateFloatAsState(
        targetValue = if (pressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ScaleAnimation"
    )

    Box(
        modifier = modifier
            .size(72.dp)
            .scale(animatedScale)
            .border(
                width = 4.dp,
                color = Color.White.copy(alpha = 0.4f),
                shape = CircleShape
            )
            .padding(3.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        pressed = true
                        awaitRelease()
                        pressed = false
                        onClick()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // 外側の透明な円
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(2.dp)
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                )
        )

        // 内側の白い円
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.White, CircleShape)
        )
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


