package app.yskuem.aimondaimaker.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * 共通の PDF プレビュー UI。
 * 左上に Download ボタン（onClickDownload）を配置。
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PdfPreviewerOverlayDialog(
    pdf: PdfDocument,
    title: String = pdf.fileName ?: "PDF",
    modifier: Modifier = Modifier,
    onCloseViewer: () -> Unit = {},
    onClickDownload: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onCloseViewer,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            BackHandler(onBack = onCloseViewer)

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            IconButton(onClick = onClickDownload) {
                                Icon(
                                    imageVector = Icons.Outlined.Download,
                                    contentDescription = "Download"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                PlatformPdfView(
                    pdf = pdf,
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                )
            }
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
