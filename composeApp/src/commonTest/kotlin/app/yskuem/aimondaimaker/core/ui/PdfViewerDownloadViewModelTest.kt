package app.yskuem.aimondaimaker.core.ui

import app.yskuem.aimondaimaker.MainDispatcherTestBase
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.core.util.PdfSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class PdfViewerDownloadViewModelTest : MainDispatcherTestBase() {

    @Test
    fun `download success updates state and forwards sanitized name`() = runTest {
        val saver = FakePdfSaver()
        val crashlytics = FakeCrashlytics()
        val viewModel = PdfViewerDownloadViewModel(
            pdfSaver = saver,
            crashlytics = crashlytics,
            mainDispatcher = testDispatcher,
            ioDispatcher = testDispatcher,
        )

        val pdfDocument = PdfDocument(
            bytes = byteArrayOf(1, 2, 3),
            fileName = "Test:Quiz",
        )

        viewModel.download(pdfDocument)

        advanceUntilIdle()

        assertIs<DataUiState.Success<Unit>>(viewModel.downloadState.value)
        assertEquals(pdfDocument.bytes.toList(), saver.savedBytes?.toList())
        val savedName = saver.savedFileName.orEmpty()
        assertTrue(savedName.endsWith(".pdf"))
        assertTrue(":" !in savedName)
        assertTrue(crashlytics.loggedMessages.isEmpty())
    }

    @Test
    fun `download failure logs and updates error state`() = runTest {
        val saver = FakePdfSaver(result = Result.failure(IllegalStateException("failed")))
        val crashlytics = FakeCrashlytics()
        val viewModel = PdfViewerDownloadViewModel(
            pdfSaver = saver,
            crashlytics = crashlytics,
            mainDispatcher = testDispatcher,
            ioDispatcher = testDispatcher,
        )

        val pdfDocument = PdfDocument(
            bytes = byteArrayOf(4, 5, 6),
            fileName = null,
        )

        viewModel.download(pdfDocument)

        advanceUntilIdle()

        assertIs<DataUiState.Error>(viewModel.downloadState.value)
        assertTrue(crashlytics.loggedMessages.any { it.contains("failed") })
    }

    private class FakePdfSaver(
        private val result: Result<Unit> = Result.success(Unit),
    ) : PdfSaver {
        var savedBytes: ByteArray? = null
        var savedFileName: String? = null

        override suspend fun savePdf(bytes: ByteArray, fileName: String): Result<Unit> {
            savedBytes = bytes
            savedFileName = fileName
            return result
        }
    }

    private class FakeCrashlytics : FirebaseCrashlytics {
        val loggedMessages = mutableListOf<String>()
        override fun log(message: String) {
            loggedMessages += message
        }
    }
}

