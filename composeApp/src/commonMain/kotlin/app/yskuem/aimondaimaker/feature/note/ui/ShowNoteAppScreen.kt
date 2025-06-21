package app.yskuem.aimondaimaker.feature.note.ui

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.domain.entity.Note
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

data class ShowNoteAppScreen(
    val note: Note,
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        NoteApp(note = note) {
            navigator?.pop()
        }
    }
}
