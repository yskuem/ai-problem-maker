package app.yskuem.aimondaimaker.feature.quiz.uiState

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.domain.entity.Quiz
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuizUiStateTest {
    @Test
    fun `should create QuizUiState with default values`() {
        val state = QuizUiState()

        assertTrue(state.quizList is DataUiState.Loading)
        assertEquals(0, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with Loading state`() {
        val state =
            QuizUiState(
                quizList = DataUiState.Loading,
                currentQuizListIndex = 0,
            )

        assertTrue(state.quizList is DataUiState.Loading)
        assertTrue(state.quizList.isLoading)
        assertEquals(0, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with Success state`() {
        val quiz1 = createTestQuiz("quiz-1", "Test Quiz 1")
        val quiz2 = createTestQuiz("quiz-2", "Test Quiz 2")
        val quizList = listOf(quiz1, quiz2)

        val state =
            QuizUiState(
                quizList = DataUiState.Success(quizList),
                currentQuizListIndex = 1,
            )

        assertTrue(state.quizList is DataUiState.Success)
        assertEquals(quizList, (state.quizList as DataUiState.Success).data)
        assertEquals(1, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with Error state`() {
        val exception = Exception("Failed to load quizzes")
        val state =
            QuizUiState(
                quizList = DataUiState.Error(exception),
                currentQuizListIndex = 0,
            )

        assertTrue(state.quizList is DataUiState.Error)
        assertEquals(exception, (state.quizList as DataUiState.Error).throwable)
        assertEquals(0, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with empty quiz list`() {
        val state =
            QuizUiState(
                quizList = DataUiState.Success(emptyList()),
                currentQuizListIndex = 0,
            )

        assertTrue(state.quizList is DataUiState.Success)
        val quizList = (state.quizList as DataUiState.Success).data
        assertTrue(quizList.isEmpty())
        assertEquals(0, state.currentQuizListIndex)
    }

    @Test
    fun `should handle negative currentQuizListIndex`() {
        val quiz = createTestQuiz("quiz-1", "Test Quiz")
        val state =
            QuizUiState(
                quizList = DataUiState.Success(listOf(quiz)),
                currentQuizListIndex = -1,
            )

        assertEquals(-1, state.currentQuizListIndex)
    }

    @Test
    fun `should handle currentQuizListIndex larger than list size`() {
        val quiz = createTestQuiz("quiz-1", "Test Quiz")
        val state =
            QuizUiState(
                quizList = DataUiState.Success(listOf(quiz)),
                currentQuizListIndex = 10,
            )

        assertEquals(10, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with single quiz`() {
        val quiz = createTestQuiz("quiz-1", "Single Quiz")
        val state =
            QuizUiState(
                quizList = DataUiState.Success(listOf(quiz)),
                currentQuizListIndex = 0,
            )

        assertTrue(state.quizList is DataUiState.Success)
        val quizList = (state.quizList as DataUiState.Success).data
        assertEquals(1, quizList.size)
        assertEquals(quiz, quizList[0])
        assertEquals(0, state.currentQuizListIndex)
    }

    @Test
    fun `should create QuizUiState with multiple quizzes`() {
        val quiz1 = createTestQuiz("quiz-1", "Quiz 1")
        val quiz2 = createTestQuiz("quiz-2", "Quiz 2")
        val quiz3 = createTestQuiz("quiz-3", "Quiz 3")
        val quizList = listOf(quiz1, quiz2, quiz3)

        val state =
            QuizUiState(
                quizList = DataUiState.Success(quizList),
                currentQuizListIndex = 2,
            )

        assertTrue(state.quizList is DataUiState.Success)
        val loadedQuizzes = (state.quizList as DataUiState.Success).data
        assertEquals(3, loadedQuizzes.size)
        assertEquals(quiz1, loadedQuizzes[0])
        assertEquals(quiz2, loadedQuizzes[1])
        assertEquals(quiz3, loadedQuizzes[2])
        assertEquals(2, state.currentQuizListIndex)
    }

    @Test
    fun `should handle quiz state transitions`() {
        val quiz = createTestQuiz("quiz-1", "Test Quiz")

        // Start with loading
        val loadingState =
            QuizUiState(
                quizList = DataUiState.Loading,
                currentQuizListIndex = 0,
            )

        // Transition to success
        val successState =
            loadingState.copy(
                quizList = DataUiState.Success(listOf(quiz)),
            )

        // Transition to error
        val errorState =
            successState.copy(
                quizList = DataUiState.Error(Exception("Network error")),
            )

        assertTrue(loadingState.quizList is DataUiState.Loading)
        assertTrue(successState.quizList is DataUiState.Success)
        assertTrue(errorState.quizList is DataUiState.Error)
    }

    @Test
    fun `should handle index updates`() {
        val quiz1 = createTestQuiz("quiz-1", "Quiz 1")
        val quiz2 = createTestQuiz("quiz-2", "Quiz 2")
        val quizList = listOf(quiz1, quiz2)

        val initialState =
            QuizUiState(
                quizList = DataUiState.Success(quizList),
                currentQuizListIndex = 0,
            )

        val updatedState = initialState.copy(currentQuizListIndex = 1)

        assertEquals(0, initialState.currentQuizListIndex)
        assertEquals(1, updatedState.currentQuizListIndex)
    }

    private fun createTestQuiz(
        id: String,
        title: String,
    ): Quiz {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        return Quiz(
            id = id,
            answer = "Option A",
            question = "Test question?",
            choices = listOf("Option A", "Option B", "Option C"),
            explanation = "Test explanation",
            projectId = "project-1",
            createdUserId = "user-1",
            groupId = "group-1",
            title = title,
            createdAt = now,
            updatedAt = now,
        )
    }
}
