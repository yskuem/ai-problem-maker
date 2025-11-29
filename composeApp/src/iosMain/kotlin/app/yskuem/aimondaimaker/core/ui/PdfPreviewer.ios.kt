package app.yskuem.aimondaimaker.core.ui

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
// 必要なら有効化： import androidx.compose.ui.interop.UIKitInteropInteractionMode

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.Foundation.*
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun PlatformPdfView(pdf: PdfDocument, modifier: Modifier) {
    // ByteArray → 一時ファイル（bytes が変わったときのみ再生成）
    val fileUrl: NSURL = remember(pdf.bytes) {
        val tempDir = NSTemporaryDirectory()
        val name = pdf.fileName ?: "preview_${NSUUID().UUIDString}.pdf"
        val path = "$tempDir/$name"
        val data = pdf.bytes.toNSData()
        NSFileManager.defaultManager.createFileAtPath(path, contents = data, attributes = null)
        NSURL.fileURLWithPath(path)
    }

    // 複数回の再コンポジションで毎回 load しないよう制御
    var loaded by remember { mutableStateOf(false) }

    UIKitView(
        modifier = modifier,
        // 新API: properties を明示（必要に応じて調整）
        properties = UIKitInteropProperties(
            // デフォルトは Cooperative。旧挙動にしたい場合だけ以下を使う
            // interactionMode = UIKitInteropInteractionMode.NonCooperative,
            // iOSのネイティブA11yを通したい場合
            // isNativeAccessibilityEnabled = true,
        ),
        factory = {
            WKWebView(
                frame = CGRectMake(0.0, 0.0, 0.0, 0.0),
                configuration = WKWebViewConfiguration()
            )
        },
        update = { webView ->
            if (!loaded) {
                val dirUrl = fileUrl.URLByDeletingLastPathComponent ?: fileUrl
                webView.loadFileURL(fileUrl, allowingReadAccessToURL = dirUrl)
                loaded = true
            }
        },
        onRelease = { webView ->
            webView.stopLoading()
        }
        // isInteractive は properties ではなく UIKitView の引数（必要ならコメントアウト解除）
        // , isInteractive = true
    )
}

/** ByteArray -> NSData 変換 */
@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData =
    this.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), length = this.size.toULong())
    }