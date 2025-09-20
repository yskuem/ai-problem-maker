package app.yskuem.aimondaimaker.feature.quiz.ui

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.FirebaseCrashlytics
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
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
class ShowQuizScreenViewModelTest : MainDispatcherTestBase() {
    private val authRepository: AuthRepository = mock()
    private val quizRepository: QuizRepository = mock()
    private val projectRepository: ProjectRepository = mock()
    private val adUseCase: AdUseCase = mock()
    private val crashlytics: FirebaseCrashlytics = mock()

    private val mockQuizList = listOf(
        Quiz(
            id = "",
            answer = "",
            question = "",
            choices = listOf("", ""),
            explanation = "",
            groupId = "",
            title = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
        ),
    )

    private val mockProject = Project(
        id = "",
        createdUserId = "",
        name = "",
        createdAt = Clock.System.now(),
        updatedAt = Clock.System.now(),
    )

    private lateinit var viewModel: ShowQuizScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend {
            quizRepository.generateFromImage(
                image = any(),
                fileName = any(),
                extension = any(),
            )
        } returns mockQuizList

        everySuspend {
            authRepository.getUserId()
        } returns ""

        everySuspend {
            projectRepository.addProject(any())
        } returns mockProject

        everySuspend {
            quizRepository.saveQuizInfo(
                projectId = any(),
                userId = any(),
                groupId = any(),
                quizTitle = any(),
            )
        } returns Unit

        everySuspend {
            quizRepository.saveQuiz(
                quiz = any(),
                projectId = any(),
                userId = any(),
            )
        } returns Unit

        everySuspend {
            crashlytics.log(any())
        } returns Unit

        viewModel = ShowQuizScreenViewModel(
            authRepository = authRepository,
            quizRepository = quizRepository,
            projectRepository = projectRepository,
            adUseCase = adUseCase,
            crashlytics = crashlytics,
        )
    }

    /**
     * クイズのロードで成功した時のパターン
     */
    @Test
    fun check_on_load_page_on_success() = runTest {
        viewModel.uiState.test {
            assertTrue(expectMostRecentItem().quizList.isLoading)
            viewModel.onLoadPage(
                imageByte = ByteArray(0),
                fileName = "",
                extension = "",
                projectId = "",
            )

            testScheduler.advanceUntilIdle()

            assertTrue(awaitItem().quizList is DataUiState.Success<List<Quiz>>)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * クイズのロードで失敗した時のパターン
     */
    @Test
    fun check_on_load_page_on_failed() = runTest {
        viewModel.uiState.test {
            assertTrue(expectMostRecentItem().quizList.isLoading)

            everySuspend {
                quizRepository.generateFromImage(
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

            assertTrue(awaitItem().quizList is DataUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }
}