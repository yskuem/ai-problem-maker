package app.yskuem.aimondaimaker.feature.select_project.ui

import MainDispatcherTestBase
import app.cash.turbine.test
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.entity.Project
import dev.mokkery.answering.returns
import dev.mokkery.answering.throwsErrorWith
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class SelectProjectScreenViewModelTest : MainDispatcherTestBase() {
    private val projectRepository: ProjectRepository = mock()
    private val adRepository: AdRepository = mock()

    private val mockProjectList = listOf(
        Project(
            id = "1",
            createdUserId = "",
            name = "Project1",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
        ),
    )

    private lateinit var viewModel: SelectProjectScreenViewModel

    @BeforeTest
    fun setup() {
        everySuspend { projectRepository.fetchProjectList() } returns mockProjectList
        everySuspend { projectRepository.updateProject(any()) } returns mockProjectList.first()
        everySuspend { projectRepository.deleteProject(any()) } returns true

        viewModel = SelectProjectScreenViewModel(
            projectRepository = projectRepository,
            adRepository = adRepository,
        )
    }

    /**
     * プロジェクト一覧のロードで成功した時のパターン
     */
    @Test
    fun check_refresh_project_list_on_success() = runTest {
        viewModel.projects.test {
            assertTrue(expectMostRecentItem().isLoading)

            viewModel.refreshProjectList()
            testScheduler.advanceUntilIdle()

            assertTrue(awaitItem() is DataUiState.Success<List<Project>>)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * プロジェクト一覧のロードで失敗した時のパターン
     */
    @Test
    fun check_refresh_project_list_on_failed() = runTest {
        viewModel.projects.test {
            assertTrue(expectMostRecentItem().isLoading)

            everySuspend { projectRepository.fetchProjectList() } throwsErrorWith "Failed!"

            viewModel.refreshProjectList()
            testScheduler.advanceUntilIdle()

            assertTrue(awaitItem() is DataUiState.Error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * プロジェクトの編集で成功した時のパターン
     */
    @Test
    fun check_edit_project_on_success() = runTest {
        viewModel.projects.test {
            assertTrue(expectMostRecentItem().isLoading)

            val target = mockProjectList.first().copy(name = "Updated")
            everySuspend { projectRepository.updateProject(any()) } returns target

            viewModel.editProject(target, mockProjectList)
            testScheduler.advanceUntilIdle()

            val result = awaitItem()
            assertTrue(result is DataUiState.Success<List<Project>>)
            assertEquals("Updated", result.data.first().name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    /**
     * プロジェクトの削除で成功した時のパターン
     */
    @Test
    fun check_delete_project_on_success() = runTest {
        viewModel.projects.test {
            assertTrue(expectMostRecentItem().isLoading)

            viewModel.deleteProject(mockProjectList.first().id, mockProjectList)
            testScheduler.advanceUntilIdle()

            val result = awaitItem()
            assertTrue(result is DataUiState.Success<List<Project>>)
            assertTrue(result.data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

