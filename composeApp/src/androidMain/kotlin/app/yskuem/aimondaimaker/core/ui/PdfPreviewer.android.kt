package app.yskuem.aimondaimaker.core.ui

import androidx.compose.runtime.Composable
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

@Composable
actual fun PlatformPdfView(pdf: PdfDocument, modifier: Modifier) {
    val context = LocalContext.current

    // ByteArray → 一時ファイル（再コンポジションの無駄書き込みを避ける）
    val tempFile = remember(pdf.bytes) {
        val f = File.createTempFile("preview_", ".pdf", context.cacheDir)
        FileOutputStream(f).use { it.write(pdf.bytes) }
        f
    }

    // 全ページを Bitmap 化（シンプル実装）
    // 大容量 PDF ではメモリ圧迫しうるため、必要に応じて可視範囲だけ描画する最適化へ差し替えてください。
    val pageBitmaps by produceState(initialValue = emptyList<Bitmap>(), key1 = tempFile) {
        val list = mutableListOf<Bitmap>()
        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        try {
            pfd = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(pfd)
            for (i in 0 until renderer.pageCount) {
                renderer.openPage(i).use { page ->
                    val scale = 2 // 解像度を上げたい場合は倍率を調整
                    val width = page.width * scale
                    val height = page.height * scale
                    val bmp = createBitmap(width, height)
                    page.render(bmp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    list += bmp
                }
            }
        } finally {
            renderer?.close()
            pfd?.close()
        }
        value = list
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(pageBitmaps) { _, bmp ->
            val ratio = bmp.width.toFloat() / bmp.height.toFloat()
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(ratio)
            )
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}