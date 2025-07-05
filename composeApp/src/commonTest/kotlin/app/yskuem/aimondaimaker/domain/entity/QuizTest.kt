package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuizTest {

    @Test
    fun `should create quiz with all required fields`() {
        // Given
        val id = "quiz-123"
        val answer = "Tokyo"
        val question = "What is the capital of Japan?"
        val choices = listOf("Tokyo", "Osaka", "Kyoto", "Hiroshima")
        val explanation = "Tokyo is the capital and largest city of Japan."
        val projectId = "project-456"
        val createdUserId = "user-789"
        val groupId = "group-101"
        val title = "Geography Quiz"
        val now = Clock.System.now()

        // When
        val quiz = Quiz(
            id = id,
            answer = answer,
            question = question,
            choices = choices,
            explanation = explanation,
            projectId = projectId,
            createdUserId = createdUserId,
            groupId = groupId,
            title = title,
            createdAt = now,
            updatedAt = now
        )

        // Then
        assertEquals(id, quiz.id)
        assertEquals(answer, quiz.answer)
        assertEquals(question, quiz.question)
        assertEquals(choices.size, quiz.choices.size)
        assertEquals(explanation, quiz.explanation)
        assertEquals(projectId, quiz.projectId)
        assertEquals(createdUserId, quiz.createdUserId)
        assertEquals(groupId, quiz.groupId)
        assertEquals(title, quiz.title)
        assertEquals(now, quiz.createdAt)
        assertEquals(now, quiz.updatedAt)
    }

    @Test
    fun `should create quiz with default empty values for optional fields`() {
        // Given
        val id = "quiz-123"
        val answer = "Tokyo"
        val question = "What is the capital of Japan?"
        val choices = listOf("Tokyo", "Osaka", "Kyoto", "Hiroshima")
        val explanation = "Tokyo is the capital and largest city of Japan."
        val groupId = "group-101"
        val title = "Geography Quiz"
        val now = Clock.System.now()

        // When
        val quiz = Quiz(
            id = id,
            answer = answer,
            question = question,
            choices = choices,
            explanation = explanation,
            groupId = groupId,
            title = title,
            createdAt = now,
            updatedAt = now
        )

        // Then
        assertEquals("", quiz.projectId)
        assertEquals("", quiz.createdUserId)
    }

    @Test
    fun `should handle empty choices list`() {
        // Given
        val quiz = Quiz(
            id = "quiz-123",
            answer = "Answer",
            question = "Question?",
            choices = emptyList(),
            explanation = "Explanation",
            groupId = "group-101",
            title = "Test Quiz",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(quiz.choices)
        assertTrue(quiz.choices.isEmpty())
    }

    @Test
    fun `should handle multiple choice questions`() {
        // Given
        val choices = listOf("A", "B", "C", "D")
        val quiz = Quiz(
            id = "quiz-123",
            answer = "A",
            question = "Choose the correct answer:",
            choices = choices,
            explanation = "A is correct because...",
            groupId = "group-101",
            title = "Multiple Choice Quiz",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(4, quiz.choices.size)
        assertTrue(quiz.choices.contains("A"))
        assertTrue(quiz.choices.contains("B"))
        assertTrue(quiz.choices.contains("C"))
        assertTrue(quiz.choices.contains("D"))
    }
}