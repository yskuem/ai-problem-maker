package app.yskuem.aimondaimaker.feature.note.ui

import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.domain.entity.Note
import androidx.navigation.NavController

@Composable
fun ShowNoteAppScreen(
    note: Note,
    navController: NavController,
) {
    NoteApp(note = note) {
        navController.popBackStack()
    }
}