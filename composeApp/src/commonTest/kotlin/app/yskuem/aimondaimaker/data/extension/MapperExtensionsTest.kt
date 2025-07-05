package app.yskuem.aimondaimaker.data.extension

import app.yskuem.aimondaimaker.data.api.response.NoteApiDto
import app.yskuem.aimondaimaker.data.api.response.QuizApiDto
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MapperExtensionsTest {

    @Test
    fun `should map QuizApiDto to Quiz domain entity`() {
        // Given
        val quizApiDto = QuizApiDto(
            id = "quiz-123",
            answer = "Tokyo",
            title = "Geography Quiz",
            groupId = "group-456",
            question = "What is the capital of Japan?",
            choices = listOf("Tokyo", "Osaka", "Kyoto", "Hiroshima"),
            explanation = "Tokyo is the capital and largest city of Japan."
        )

        // When
        val quiz = quizApiDto.toDomain()

        // Then
        assertEquals(quizApiDto.id, quiz.id)
        assertEquals(quizApiDto.answer, quiz.answer)
        assertEquals(quizApiDto.title, quiz.title)
        assertEquals(quizApiDto.groupId, quiz.groupId)
        assertEquals(quizApiDto.question, quiz.question)
        assertEquals(quizApiDto.explanation, quiz.explanation)
        
        // Check that choices are shuffled (order might be different)
        assertEquals(quizApiDto.choices.size, quiz.choices.size)
        assertTrue(quiz.choices.containsAll(quizApiDto.choices))
        
        // Check that timestamps are set
        assertTrue(quiz.createdAt <= Clock.System.now())
        assertTrue(quiz.updatedAt <= Clock.System.now())
    }

    @Test
    fun `should shuffle choices when mapping QuizApiDto to Quiz`() {
        // Given
        val originalChoices = listOf("A", "B", "C", "D")
        val quizApiDto = QuizApiDto(
            id = "quiz-123",
            answer = "A",
            title = "Test Quiz",
            groupId = "group-456",
            question = "Choose A:",
            choices = originalChoices,
            explanation = "A is correct."
        )

        // When - Map multiple times to check shuffling
        val quiz1 = quizApiDto.toDomain()
        val quiz2 = quizApiDto.toDomain()
        val quiz3 = quizApiDto.toDomain()

        // Then - All should contain the same elements but order might differ
        assertTrue(quiz1.choices.containsAll(originalChoices))
        assertTrue(quiz2.choices.containsAll(originalChoices))
        assertTrue(quiz3.choices.containsAll(originalChoices))
        
        // Note: Due to shuffling, order might be different
        // We can't guarantee order difference in tests, but we can check elements are preserved
        assertEquals(originalChoices.size, quiz1.choices.size)
        assertEquals(originalChoices.size, quiz2.choices.size)
        assertEquals(originalChoices.size, quiz3.choices.size)
    }

    @Test
    fun `should map Project to ProjectDto`() {
        // Given
        val now = Clock.System.now()
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )

        // When
        val projectDto = project.toDTO()

        // Then
        assertEquals(project.id, projectDto.id)
        assertEquals(project.createdUserId, projectDto.createdUserId)
        assertEquals(project.name, projectDto.name)
        assertEquals(project.createdAt, projectDto.createdAt)
        assertEquals(project.updatedAt, projectDto.updatedAt)
    }

    @Test
    fun `should map ProjectDto to Project domain entity`() {
        // Given
        val now = Clock.System.now()
        val projectDto = ProjectDto(
            id = "project-123",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )

        // When
        val project = projectDto.toDomain()

        // Then
        assertEquals(projectDto.id, project.id)
        assertEquals(projectDto.createdUserId, project.createdUserId)
        assertEquals(projectDto.name, project.name)
        assertEquals(projectDto.createdAt, project.createdAt)
        assertEquals(projectDto.updatedAt, project.updatedAt)
    }

    @Test
    fun `should map NoteApiDto to Note domain entity`() {
        // Given
        val noteApiDto = NoteApiDto(
            id = "note-123",
            title = "Important Notes",
            html = "<h1>Title</h1><p>This is a note content.</p>"
        )

        // When
        val note = noteApiDto.toDomain()

        // Then
        assertEquals(noteApiDto.id, note.id)
        assertEquals(noteApiDto.title, note.title)
        assertEquals(noteApiDto.html, note.html)
        
        // Check that timestamps are set
        assertTrue(note.createdAt <= Clock.System.now())
        assertTrue(note.updatedAt <= Clock.System.now())
    }

    @Test
    fun `should handle empty values in mapping`() {
        // Given
        val quizApiDto = QuizApiDto(
            id = "",
            answer = "",
            title = "",
            groupId = "",
            question = "",
            choices = emptyList(),
            explanation = ""
        )

        // When
        val quiz = quizApiDto.toDomain()

        // Then
        assertEquals("", quiz.id)
        assertEquals("", quiz.answer)
        assertEquals("", quiz.title)
        assertEquals("", quiz.groupId)
        assertEquals("", quiz.question)
        assertEquals("", quiz.explanation)
        assertTrue(quiz.choices.isEmpty())
    }

    @Test
    fun `should handle unicode characters in mapping`() {
        // Given
        val noteApiDto = NoteApiDto(
            id = "note-123",
            title = "テストノート 📝",
            html = "<p>日本語のコンテンツ 🌸</p>"
        )

        // When
        val note = noteApiDto.toDomain()

        // Then
        assertEquals("テストノート 📝", note.title)
        assertEquals("<p>日本語のコンテンツ 🌸</p>", note.html)
    }
}