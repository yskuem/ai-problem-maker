package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.PdfSaver
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSData
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToURL

actual val pdfSaverPlatformModule: Module =
    module {
        single<PdfSaver> {
            IosPdfSaver()
        }
    }

private class IosPdfSaver : PdfSaver {
    override suspend fun savePdf(bytes: ByteArray, fileName: String): Result<Unit> =
        withContext(Dispatchers.Default) {
            runCatching {
                val documentsDirectory =
                    NSFileManager.defaultManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).firstObject as NSURL
                val targetUrl = documentsDirectory.URLByAppendingPathComponent(fileName)
                val data = bytes.toNSData()
                if (!data.writeToURL(targetUrl, true)) {
                    throw IllegalStateException("Failed to write pdf to ${targetUrl.path.orEmpty()}")
                }
            }
        }
}

private fun ByteArray.toNSData(): NSData =
    usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }

