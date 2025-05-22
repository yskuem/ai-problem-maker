package app.yskuem.aimondaimaker.feature.note.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.domain.entity.Note
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData


@Composable
fun NoteApp(
    note: Note,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val htmlState = rememberWebViewStateWithHTMLData(note.html)
        WebView(
            state = htmlState,
            modifier = Modifier.fillMaxSize()
        )
    }
}