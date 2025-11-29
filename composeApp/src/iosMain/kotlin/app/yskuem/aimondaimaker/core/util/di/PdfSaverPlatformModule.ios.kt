package app.yskuem.aimondaimaker.core.util.di

import app.yskuem.aimondaimaker.core.util.PdfSaver
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
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
import platform.Foundation.create
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
                // Documents ディレクトリの NSURL を非 null で取得
                val documentsDirectory: NSURL =
                    (NSFileManager.defaultManager
                        .URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
                        .firstOrNull() as? NSURL)
                        ?: error("Documents directory not found")

                // ここが nullable（NSURL?）なので非 null を強制
                val targetUrl: NSURL =
                    requireNotNull(documentsDirectory.URLByAppendingPathComponent(fileName)) {
                        "Failed to build target URL for $fileName"
                    }

                val data = bytes.toNSData()

                if (!data.writeToURL(targetUrl, atomically = true)) {
                    throw IllegalStateException(
                        "Failed to write pdf to ${targetUrl.path.orEmpty()}"
                    )
                }
            }
        }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData =
    usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }