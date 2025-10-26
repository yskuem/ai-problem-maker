package app.yskuem.aimondaimaker.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

/**
 * 共通の PDF プレビュー UI。
 * 左上に Download ボタン（onClickDownload）を配置。
 */
data class PdfPreviewer(
    val pdf: PdfDocument,
    val title: String = pdf.fileName ?: "PDF",
    val modifier: Modifier = Modifier,
    val onClickDownload: () -> Unit = {},
    val onClose: () -> Unit = {},
): Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        BackHandler {
            onClose()
            navigator?.pop()
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = onClickDownload) {
                            Icon(imageVector = Icons.Outlined.Download, contentDescription = "Download")
                        }
                    }
                )
            }
        ) { innerPadding ->
            PlatformPdfView(
                pdf = pdf,
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

/** プラットフォーム別に埋める PDF ビュー本体 */
@Composable
expect fun PlatformPdfView(pdf: PdfDocument, modifier: Modifier = Modifier)


data class PdfDocument(
    val bytes: ByteArray,
    val fileName: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PdfDocument

        if (!bytes.contentEquals(other.bytes)) return false
        if (fileName != other.fileName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + (fileName?.hashCode() ?: 0)
        return result
    }
}
