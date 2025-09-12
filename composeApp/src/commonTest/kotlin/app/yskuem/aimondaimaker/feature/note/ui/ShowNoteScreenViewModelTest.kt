package app.yskuem.aimondaimaker.feature.note.ui

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.Project
import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import dev.mokkery.answering.returns
import dev.mokkery.answering.throwsErrorWith
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ShowNoteScreenViewModelTest : MainDispatcherTestBase() {
    private val authRepository: AuthRepository = mock()
    private val noteRepository: NoteRepository = mock()
    private val projectRepository: ProjectRepository = mock()
    private val adUseCase: AdUseCase = mock()
    private val crashlytics: FirebaseCrashlytics = mock()

    private val mockNoteObj = Note(
        id = "",
        title = "",
        html = "",
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now()
    )

    private val mockProjectObj = Project(
        id = "",
        createdUserId = "",
        name = "",
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now(),
    )

    private lateinit var viewModel: ShowNoteScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend {
            noteRepository.generateFromImage(
                image = any(),
                fileName = any(),
                extension = any(),
            )
        } returns mockNoteObj

        everySuspend {
            authRepository.getUserId()
        } returns ""

        everySuspend {
            projectRepository.addProject(any())
        } returns mockProjectObj


        everySuspend {
            noteRepository.saveNote(
                note = any(),
                projectId = any(),
                userId = any(),
            )
        } returns Unit

        everySuspend {
            crashlytics.log(any())
        } returns Unit


        viewModel = ShowNoteScreenViewModel(
            authRepository = authRepository,
            noteRepository = noteRepository,
            projectRepository = projectRepository,
            adUseCase = adUseCase,
            crashlytics = crashlytics,
        )
    }

    /**
     * ノートのロードで成功した時のパターン
     */
    @Test
    fun check_on_load_page_on_success() = runTest {
        viewModel.uiState.test {

            assertTrue(expectMostRecentItem().note.isLoading)
            viewModel.onLoadPage(
                imageByte = ByteArray(0),
                fileName = "",
                extension = "",
                projectId = "",
            )

            testScheduler.advanceUntilIdle()

            assertTrue (awaitItem().note is DataUiState.Success<Note>)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * ノートのロードで失敗した時のパターン
     */
    @Test
    fun check_on_load_page_on_failed() = runTest {
        viewModel.uiState.test {
            assertTrue(expectMostRecentItem().note.isLoading)

            everySuspend {
                noteRepository.generateFromImage(
                    image = any(),
                    fileName = any(),
                    extension = any(),
                )
            } throwsErrorWith "Failed!"

            viewModel.onLoadPage(
                imageByte = ByteArray(0),
                fileName = "",
                extension = "",
                projectId = "",
            )

            testScheduler.advanceUntilIdle()

            assertTrue (awaitItem().note is DataUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}