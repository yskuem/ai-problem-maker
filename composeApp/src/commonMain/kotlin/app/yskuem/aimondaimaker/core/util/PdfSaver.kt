package app.yskuem.aimondaimaker.core.util

/**
 * Saves PDF data into a platform specific persistent storage.
 */
interface PdfSaver {
    /**
     * Persist the provided [bytes] using the supplied [fileName].
     * The [fileName] parameter must include the extension that will
     * be used when saving the file.
     */
    suspend fun savePdf(
        bytes: ByteArray,
        fileName: String,
    ): Result<Unit>
}
