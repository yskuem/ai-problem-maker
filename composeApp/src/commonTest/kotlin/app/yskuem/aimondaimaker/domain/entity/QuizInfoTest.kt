package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class QuizInfoTest {

    @Test
    fun `should create quiz info with all required fields`() {
        // Given
        val projectId = "project-123"
        val groupId = "group-456"
        val createdUserId = "user-789"
        val name = "Math Quiz"
        val now = Clock.System.now()

        // When
        val quizInfo = QuizInfo(
            projectId = projectId,
            groupId = groupId,
            createdUserId = createdUserId,
            name = name,
            updatedAt = now,
            createdAt = now
        )

        // Then
        assertEquals(projectId, quizInfo.projectId)
        assertEquals(groupId, quizInfo.groupId)
        assertEquals(createdUserId, quizInfo.createdUserId)
        assertEquals(name, quizInfo.name)
        assertEquals(now, quizInfo.updatedAt)
        assertEquals(now, quizInfo.createdAt)
    }

    @Test
    fun `should handle empty project id`() {
        // Given
        val quizInfo = QuizInfo(
            projectId = "",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertNotNull(quizInfo.projectId)
        assertTrue(quizInfo.projectId.isEmpty())
    }

    @Test
    fun `should handle empty group id`() {
        // Given
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertNotNull(quizInfo.groupId)
        assertTrue(quizInfo.groupId.isEmpty())
    }

    @Test
    fun `should handle empty created user id`() {
        // Given
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "",
            name = "Math Quiz",
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertNotNull(quizInfo.createdUserId)
        assertTrue(quizInfo.createdUserId.isEmpty())
    }

    @Test
    fun `should handle empty name`() {
        // Given
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "",
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertNotNull(quizInfo.name)
        assertTrue(quizInfo.name.isEmpty())
    }

    @Test
    fun `should handle unicode characters in name`() {
        // Given
        val unicodeName = "数学クイズ 📚"
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = unicodeName,
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertEquals(unicodeName, quizInfo.name)
    }

    @Test
    fun `should handle different created and updated times`() {
        // Given
        val createdAt = Clock.System.now()
        val updatedAt = Clock.System.now()
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = updatedAt,
            createdAt = createdAt
        )

        // Then
        assertEquals(createdAt, quizInfo.createdAt)
        assertEquals(updatedAt, quizInfo.updatedAt)
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val now = Clock.System.now()
        val quizInfo1 = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = now,
            createdAt = now
        )
        val quizInfo2 = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = now,
            createdAt = now
        )
        val quizInfo3 = QuizInfo(
            projectId = "project-456",
            groupId = "group-456",
            createdUserId = "user-789",
            name = "Math Quiz",
            updatedAt = now,
            createdAt = now
        )

        // When & Then
        assertEquals(quizInfo1, quizInfo2)
        assertTrue(quizInfo1 == quizInfo2)
        assertTrue(quizInfo1 != quizInfo3)
    }

    @Test
    fun `should handle long quiz names`() {
        // Given
        val longName = "This is a very long quiz name that contains many characters and should be handled properly by the QuizInfo entity without any issues or truncation"
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = longName,
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertEquals(longName, quizInfo.name)
        assertTrue(quizInfo.name.length > 50)
    }

    @Test
    fun `should handle special characters in quiz name`() {
        // Given
        val specialName = "Quiz #1 - Test & Review (2024)"
        val quizInfo = QuizInfo(
            projectId = "project-123",
            groupId = "group-456",
            createdUserId = "user-789",
            name = specialName,
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertEquals(specialName, quizInfo.name)
    }

    @Test
    fun `should handle various id formats`() {
        // Given
        val uuidProjectId = "550e8400-e29b-41d4-a716-446655440000"
        val numericGroupId = "12345"
        val emailUserId = "user@example.com"
        
        val quizInfo = QuizInfo(
            projectId = uuidProjectId,
            groupId = numericGroupId,
            createdUserId = emailUserId,
            name = "Test Quiz",
            updatedAt = Clock.System.now(),
            createdAt = Clock.System.now()
        )

        // Then
        assertEquals(uuidProjectId, quizInfo.projectId)
        assertEquals(numericGroupId, quizInfo.groupId)
        assertEquals(emailUserId, quizInfo.createdUserId)
    }
}