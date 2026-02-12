package app.yskuem.aimondaimaker.domain.entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.Instant

class QuizTest {

    @Test
    fun `should create quiz with valid data`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val choices = listOf("Option A", "Option B", "Option C", "Option D")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "Option B",
            question = "What is the correct answer?",
            choices = choices,
            explanation = "This is the correct answer because...",
            projectId = "project-1",
            createdUserId = "user-1",
            groupId = "group-1",
            title = "Test Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertEquals("quiz-1", quiz.id)
        assertEquals("Option B", quiz.answer)
        assertEquals("What is the correct answer?", quiz.question)
        assertEquals(choices, quiz.choices)
        assertEquals("This is the correct answer because...", quiz.explanation)
        assertEquals("project-1", quiz.projectId)
        assertEquals("user-1", quiz.createdUserId)
        assertEquals("group-1", quiz.groupId)
        assertEquals("Test Quiz", quiz.title)
        assertEquals(now, quiz.createdAt)
        assertEquals(now, quiz.updatedAt)
    }

    @Test
    fun `should handle empty project and user IDs with defaults`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "Option B",
            question = "What is the correct answer?",
            choices = listOf("Option A", "Option B", "Option C"),
            explanation = "This is correct",
            groupId = "group-1",
            title = "Test Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertEquals("", quiz.projectId)
        assertEquals("", quiz.createdUserId)
    }

    @Test
    fun `should handle multiple choice options`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val choices = listOf("A", "B", "C", "D", "E")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "C",
            question = "Multiple choice question",
            choices = choices,
            explanation = "Answer is C",
            groupId = "group-1",
            title = "Test Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(5, quiz.choices.size)
        assertTrue(quiz.choices.contains("C"))
        assertTrue(quiz.choices.contains(quiz.answer))
    }

    @Test
    fun `should handle Japanese text in question and choices`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val choices = listOf("選択肢A", "選択肢B", "選択肢C", "選択肢D")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "選択肢B",
            question = "正しい答えは何ですか？",
            choices = choices,
            explanation = "これが正しい答えです",
            groupId = "group-1",
            title = "テストクイズ",
            createdAt = now,
            updatedAt = now
        )

        assertEquals("正しい答えは何ですか？", quiz.question)
        assertEquals("選択肢B", quiz.answer)
        assertEquals("テストクイズ", quiz.title)
        assertTrue(quiz.choices.contains("選択肢B"))
    }

    @Test
    fun `should handle empty choices list`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "True",
            question = "True or False question",
            choices = emptyList(),
            explanation = "This is a true/false question",
            groupId = "group-1",
            title = "Test Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(quiz.choices.isEmpty())
    }

    @Test
    fun `should handle long explanation text`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val longExplanation = "This is a very long explanation that contains multiple sentences. " +
                "It explains in detail why the answer is correct. " +
                "It provides context and additional information. " +
                "This helps students understand the concept better."
        val quiz = Quiz(
            id = "quiz-1",
            answer = "Option A",
            question = "Test question",
            choices = listOf("Option A", "Option B"),
            explanation = longExplanation,
            groupId = "group-1",
            title = "Test Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(longExplanation, quiz.explanation)
        assertTrue(quiz.explanation.length > 100)
    }

    @Test
    fun `should handle boolean-style choices`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val choices = listOf("True", "False")
        val quiz = Quiz(
            id = "quiz-1",
            answer = "True",
            question = "Is this statement correct?",
            choices = choices,
            explanation = "The statement is true",
            groupId = "group-1",
            title = "True/False Quiz",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(2, quiz.choices.size)
        assertTrue(quiz.choices.contains("True"))
        assertTrue(quiz.choices.contains("False"))
        assertEquals("True", quiz.answer)
    }
}