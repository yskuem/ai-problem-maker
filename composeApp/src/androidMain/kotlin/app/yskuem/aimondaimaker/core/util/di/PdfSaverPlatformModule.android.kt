package app.yskuem.aimondaimaker.core.util.di

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import app.yskuem.aimondaimaker.core.util.PdfSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

actual val pdfSaverPlatformModule: Module =
    module {
        single<PdfSaver> {
            AndroidPdfSaver(
                context = androidContext(),
            )
        }
    }

private class AndroidPdfSaver(
    private val context: Context,
) : PdfSaver {

    override suspend fun savePdf(bytes: ByteArray, fileName: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val sanitizedFileName = fileName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveWithMediaStore(bytes, sanitizedFileName)
                } else {
                    saveToAppDownloads(bytes, sanitizedFileName)
                }
            }
        }

    private fun saveWithMediaStore(bytes: ByteArray, fileName: String) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues)
            ?: throw IOException("Failed to create MediaStore entry")

        resolver.openOutputStream(itemUri)?.use { outputStream ->
            outputStream.write(bytes)
        } ?: throw IOException("Failed to open MediaStore output stream")

        val update = ContentValues().apply {
            put(MediaStore.Downloads.IS_PENDING, 0)
        }
        resolver.update(itemUri, update, null, null)
    }

    private fun saveToAppDownloads(bytes: ByteArray, fileName: String) {
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            ?: throw IOException("Unable to access downloads directory")

        if (!downloadsDir.exists() && !downloadsDir.mkdirs()) {
            throw IOException("Failed to create downloads directory")
        }

        val outputFile = File(downloadsDir, fileName)
        FileOutputStream(outputFile).use { stream ->
            stream.write(bytes)
        }
    }
}

