package app.yskuem.aimondaimaker.core.ui

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile

// 想定: 別所で定義されている PDF データラッパー
// data class PdfDocument(val bytes: ByteArray)

@Composable
actual fun PlatformPdfView(
    pdf: PdfDocument,
    modifier: Modifier,
) {
    val context = LocalContext.current

    // PDF → 画像の変換をバックグラウンドで実施し、成功/失敗を保持
    val renderResult by produceState<Result<List<Bitmap>>>(
        initialValue = Result.success(emptyList()),
        key1 = pdf.bytes,
    ) {
        value =
            withContext(Dispatchers.IO) {
                var tempFile: File? = null
                var pfd: ParcelFileDescriptor? = null
                var renderer: PdfRenderer? = null
                try {
                    // 一時ファイル作成と書き込み（IOスレッド）
                    tempFile = File.createTempFile("preview_", ".pdf", context.cacheDir)
                    FileOutputStream(tempFile).use { it.write(pdf.bytes) }

                    // 簡易ヘッダ検査（壊れ/非PDFを早期に弾く）
                    RandomAccessFile(tempFile, "r").use { raf ->
                        val header = ByteArray(5)
                        val read = raf.read(header)
                        if (read < 5 || String(header) != "%PDF-") {
                            throw IllegalArgumentException("Not a PDF header")
                        }
                    }

                    // PdfRenderer 初期化
                    pfd = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                    renderer = PdfRenderer(pfd)

                    // 全ページをレンダリング（必要に応じて可視範囲描画へ最適化すること）
                    val pages = mutableListOf<Bitmap>()
                    for (i in 0 until renderer.pageCount) {
                        renderer.openPage(i).use { page ->
                            val scale = 2
                            val width = page.width * scale
                            val height = page.height * scale
                            // ARGB_8888 を明示
                            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                            page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            pages += bmp
                        }
                    }
                    Result.success(pages)
                } catch (t: Throwable) {
                    // 代表的な原因: フォーマット不正/破損/パスワード付PDF
                    Log.e("PlatformPdfView", "PDF render failed", t)
                    Result.failure(t)
                } finally {
                    try {
                        renderer?.close()
                    } catch (_: Throwable) {
                    }
                    try {
                        pfd?.close()
                    } catch (_: Throwable) {
                    }
                    try {
                        tempFile?.delete()
                    } catch (_: Throwable) {
                    }
                }
            }
    }

    val pagesOrNull = renderResult.getOrNull()
    if (pagesOrNull != null && pagesOrNull.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(pagesOrNull) { _, bmp ->
                val ratio = bmp.width.toFloat() / bmp.height.toFloat()
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio),
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    } else if (renderResult.isFailure) {
        val cause = renderResult.exceptionOrNull()
        // ユーザー向けに簡潔なメッセージ
        BasicText(
            text =
                buildString {
                    append("PDFを読み込めませんでした。")
                    cause?.message?.let { append(" 原因: ").append(it) }
                },
            modifier = modifier.padding(16.dp),
        )
    } else {
        // 読み込み中プレースホルダ（必要ならローディングUIへ置換）
        Spacer(modifier = modifier.height(1.dp))
    }
}
