package app.yskuem.aimondaimaker.feature.note.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.yskuem.aimondaimaker.core.ui.components.AppTopBar
import app.yskuem.aimondaimaker.domain.entity.Note
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteApp(
    note: Note,
    onBack: () -> Unit,
) {
    // Scaffold provides an app bar and content area
    Scaffold(
        topBar = {
            AppTopBar(
                title = note.title,
                onBack = onBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            val htmlState = rememberWebViewStateWithHTMLData(note.html)
            WebView(
                state = htmlState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
