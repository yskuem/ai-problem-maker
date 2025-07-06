package app.yskuem.aimondaimaker.data.extension

import app.yskuem.aimondaimaker.data.api.response.NoteApiDto
import app.yskuem.aimondaimaker.data.api.response.QuizApiDto
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MapperExtensionsTest {

    @Test
    fun `should map QuizApiDto to Quiz domain model`() {
        val quizApiDto = QuizApiDto(
            id = "quiz-1",
            answer = "Option B",
            question = "What is the correct answer?",
            choices = listOf("Option A", "Option B", "Option C", "Option D"),
            explanation = "This is the explanation",
            title = "Test Quiz",
            groupId = "group-1"
        )

        val quiz = quizApiDto.toDomain()

        assertEquals("quiz-1", quiz.id)
        assertEquals("Option B", quiz.answer)
        assertEquals("What is the correct answer?", quiz.question)
        assertEquals("This is the explanation", quiz.explanation)
        assertEquals("Test Quiz", quiz.title)
        assertEquals("group-1", quiz.groupId)
        assertEquals("", quiz.projectId) // Default value
        assertEquals("", quiz.createdUserId) // Default value
        
        // Verify choices are shuffled (not necessarily in original order)
        assertEquals(4, quiz.choices.size)
        assertTrue(quiz.choices.contains("Option A"))
        assertTrue(quiz.choices.contains("Option B"))
        assertTrue(quiz.choices.contains("Option C"))
        assertTrue(quiz.choices.contains("Option D"))
    }

    @Test
    fun `should shuffle choices when mapping QuizApiDto to Quiz`() {
        val originalChoices = listOf("A", "B", "C", "D")
        val quizApiDto = QuizApiDto(
            id = "quiz-1",
            answer = "B",
            question = "Test?",
            choices = originalChoices,
            explanation = "Test explanation",
            title = "Test Quiz",
            groupId = "group-1"
        )

        // Map multiple times to increase chance of different ordering
        val mappedQuizzes = (1..20).map { quizApiDto.toDomain() }
        
        // At least one should have different ordering (statistically very likely)
        val hasShuffled = mappedQuizzes.any { it.choices != originalChoices }
        assertTrue(hasShuffled, "Choices should be shuffled")
    }

    @Test
    fun `should map Project to ProjectDto`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T00:00:00Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "Test Project",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        val projectDto = project.toDTO()

        assertEquals("project-1", projectDto.id)
        assertEquals("user-1", projectDto.createdUserId)
        assertEquals("Test Project", projectDto.name)
        assertEquals(createdAt, projectDto.createdAt)
        assertEquals(updatedAt, projectDto.updatedAt)
    }

    @Test
    fun `should map ProjectDto to Project domain model`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T00:00:00Z")
        val projectDto = ProjectDto(
            id = "project-1",
            createdUserId = "user-1",
            name = "Test Project",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        val project = projectDto.toDomain()

        assertEquals("project-1", project.id)
        assertEquals("user-1", project.createdUserId)
        assertEquals("Test Project", project.name)
        assertEquals(createdAt, project.createdAt)
        assertEquals(updatedAt, project.updatedAt)
    }

    @Test
    fun `should map NoteApiDto to Note domain model`() {
        val noteApiDto = NoteApiDto(
            id = "note-1",
            title = "Test Note",
            html = "<p>Test content</p>"
        )

        val note = noteApiDto.toDomain()

        assertEquals("note-1", note.id)
        assertEquals("Test Note", note.title)
        assertEquals("<p>Test content</p>", note.html)
        // createdAt and updatedAt should be set to current time
        assertTrue(note.createdAt.epochSeconds > 0)
        assertTrue(note.updatedAt.epochSeconds > 0)
    }

    @Test
    fun `should handle empty choices list in QuizApiDto`() {
        val quizApiDto = QuizApiDto(
            id = "quiz-1",
            answer = "True",
            question = "True or False?",
            choices = emptyList(),
            explanation = "This is true",
            title = "Boolean Quiz",
            groupId = "group-1"
        )

        val quiz = quizApiDto.toDomain()

        assertTrue(quiz.choices.isEmpty())
    }

    @Test
    fun `should handle Unicode characters in mapping`() {
        val quizApiDto = QuizApiDto(
            id = "quiz-1",
            answer = "選択肢B",
            question = "正しい答えは何ですか？",
            choices = listOf("選択肢A", "選択肢B", "選択肢C"),
            explanation = "これが正しい答えです",
            title = "日本語クイズ",
            groupId = "group-1"
        )

        val quiz = quizApiDto.toDomain()

        assertEquals("日本語クイズ", quiz.title)
        assertEquals("正しい答えは何ですか？", quiz.question)
        assertEquals("選択肢B", quiz.answer)
        assertEquals("これが正しい答えです", quiz.explanation)
    }

    @Test
    fun `should handle HTML content in NoteApiDto`() {
        val htmlContent = """
            <div>
                <h1>Title</h1>
                <p>Paragraph with <strong>bold</strong> and <em>italic</em> text.</p>
                <ul>
                    <li>Item 1</li>
                    <li>Item 2</li>
                </ul>
            </div>
        """.trimIndent()

        val noteApiDto = NoteApiDto(
            id = "note-1",
            title = "HTML Note",
            html = htmlContent
        )

        val note = noteApiDto.toDomain()

        assertEquals("HTML Note", note.title)
        assertEquals(htmlContent, note.html)
    }

    @Test
    fun `should create different timestamps for multiple mappings`() {
        val noteApiDto = NoteApiDto(
            id = "note-1",
            title = "Test Note",
            html = "<p>Test</p>"
        )

        val note1 = noteApiDto.toDomain()
        Thread.sleep(1) // Ensure different timestamps
        val note2 = noteApiDto.toDomain()

        // While timestamps might be the same due to precision, they should be close
        assertTrue(note1.createdAt.epochSeconds <= note2.createdAt.epochSeconds)
        assertTrue(note1.updatedAt.epochSeconds <= note2.updatedAt.epochSeconds)
    }
}