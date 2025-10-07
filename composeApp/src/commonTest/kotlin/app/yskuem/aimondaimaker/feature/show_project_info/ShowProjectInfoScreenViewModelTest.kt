package app.yskuem.aimondaimaker.feature.show_project_info

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
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
class ShowProjectInfoScreenViewModelTest : MainDispatcherTestBase() {
    private val quizRepository: QuizRepository = mock()
    private val noteRepository: NoteRepository = mock()

    private val mockQuizInfoList =
        listOf(
            QuizInfo(
                projectId = "",
                groupId = "",
                createdUserId = "",
                name = "",
                updatedAt = Clock.System.now(),
                createdAt = Clock.System.now(),
            ),
        )

    private val mockNoteList =
        listOf(
            Note(
                id = "",
                title = "",
                html = "",
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
            ),
        )

    private lateinit var viewModel: ShowProjectInfoScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend { quizRepository.fetchQuizInfoList(any()) } returns mockQuizInfoList
        everySuspend { noteRepository.fetchNotes(any()) } returns mockNoteList

        viewModel =
            ShowProjectInfoScreenViewModel(
                quizRepository = quizRepository,
                noteRepository = noteRepository,
                projectId = "",
            )
    }

    /**
     * クイズ情報のロードで成功した時のパターン
     */
    @Test
    fun check_fetch_quiz_info_on_success() =
        runTest {
            viewModel.uiState.test {
                assertTrue(expectMostRecentItem().quizInfoList.isLoading)

                testScheduler.advanceUntilIdle()

                assertTrue(awaitItem().quizInfoList is DataUiState.Success<List<QuizInfo>>)
                cancelAndIgnoreRemainingEvents()
            }
        }

    /**
     * クイズ情報のロードで失敗した時のパターン
     */
    @Test
    fun check_fetch_quiz_info_on_failed() =
        runTest {
            everySuspend { quizRepository.fetchQuizInfoList(any()) } throwsErrorWith "Failed!"

            // viewmodelを再定義してinit内のfetchQuizInfoListを実行
            viewModel =
                ShowProjectInfoScreenViewModel(
                    quizRepository = quizRepository,
                    noteRepository = noteRepository,
                    projectId = "",
                )

            viewModel.uiState.test {
                assertTrue(expectMostRecentItem().quizInfoList.isLoading)

                testScheduler.advanceUntilIdle()

                assertTrue(awaitItem().quizInfoList is DataUiState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }

    /**
     * タブをタップした時のノートリストロードで成功したパターン
     */
    @Test
    fun check_on_tap_tab_on_success() =
        runTest {
            viewModel.uiState.test {
                assertTrue(expectMostRecentItem().noteList.isLoading)

                viewModel.onTapTab(1)

                testScheduler.advanceUntilIdle()

                assertTrue(awaitItem().noteList is DataUiState.Success<List<Note>>)
                cancelAndIgnoreRemainingEvents()
            }
        }

    /**
     * タブをタップした時のノートリストロードで失敗したパターン
     */
    @Test
    fun check_on_tap_tab_on_failed() =
        runTest {
            viewModel.uiState.test {
                assertTrue(expectMostRecentItem().noteList.isLoading)

                everySuspend { noteRepository.fetchNotes(any()) } throwsErrorWith "Failed!"
                viewModel.onTapTab(1)

                testScheduler.advanceUntilIdle()

                assertTrue(awaitItem().noteList is DataUiState.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
}
