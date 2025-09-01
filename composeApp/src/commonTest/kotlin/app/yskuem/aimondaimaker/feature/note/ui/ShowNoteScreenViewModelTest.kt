package app.yskuem.aimondaimaker.feature.note.ui

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.Project
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.usecase.AdUseCase
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.mockMany
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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


        viewModel = ShowNoteScreenViewModel(
            authRepository = authRepository,
            noteRepository = noteRepository,
            projectRepository = projectRepository,
            adUseCase = adUseCase,
            crashlytics = crashlytics,
        )
    }

    @Test
    fun check_on_load_page() = runTest {
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
}