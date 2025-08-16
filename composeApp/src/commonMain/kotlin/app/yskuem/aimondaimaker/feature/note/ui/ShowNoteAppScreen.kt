package app.yskuem.aimondaimaker.feature.note.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.yskuem.aimondaimaker.domain.entity.Note
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

data class ShowNoteAppScreen(
    val note: Note,
) : Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val viewmodel = koinScreenModel<ShowNoteScreenViewModel>()
        val navigator = LocalNavigator.current

        BackHandler {
            viewmodel.showInterstitialAd()
            navigator?.pop()
        }

        NoteApp(note = note) {
            viewmodel.showInterstitialAd()
            navigator?.pop()
        }
    }
}
