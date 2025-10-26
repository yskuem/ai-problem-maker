package app.yskuem.aimondaimaker.core.ui

import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.core.util.PdfSaver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class PdfViewerDownloadViewModel(
    private val pdfSaver: PdfSaver,
    private val crashlytics: FirebaseCrashlytics,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    private val coroutineScope = CoroutineScope(SupervisorJob() + mainDispatcher)

    private val _downloadState = MutableStateFlow<DataUiState<Unit>>(DataUiState.Initial)
    val downloadState: StateFlow<DataUiState<Unit>> = _downloadState.asStateFlow()

    fun download(pdfDocument: PdfDocument) {
        val resolvedFileName = buildFileName(pdfDocument.fileName)
        coroutineScope.launch {
            _downloadState.value = DataUiState.Loading
            val result = withContext(ioDispatcher) {
                pdfSaver.savePdf(pdfDocument.bytes, resolvedFileName)
            }

            result
                .onSuccess {
                    _downloadState.value = DataUiState.Success(Unit)
                }
                .onFailure { throwable ->
                    val message = throwable.message ?: throwable::class.simpleName.orEmpty()
                    crashlytics.log("Pdf download failed: $message")
                    _downloadState.value = DataUiState.Error(throwable)
                }
        }
    }

    fun reset() {
        _downloadState.value = DataUiState.Initial
    }

    fun clear() {
        coroutineScope.cancel()
    }

    private fun buildFileName(original: String?): String {
        val timestamp = Clock.System.now().toEpochMilliseconds()
        val base = original?.takeIf { it.isNotBlank() } ?: "quiz_$timestamp"
        val sanitized = base.replace(invalidFileNameChars, "_").ifBlank { "quiz_$timestamp" }
        return if (sanitized.endsWith(".pdf", ignoreCase = true)) sanitized else "$sanitized.pdf"
    }

    private companion object {
        val invalidFileNameChars = Regex("""[\\/:*?"<>|]""")
    }
}

