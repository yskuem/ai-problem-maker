package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NoteTest {

    @Test
    fun `should create note with all required fields`() {
        // Given
        val id = "note-123"
        val title = "Important Notes"
        val html = "<h1>Title</h1><p>This is a note content.</p>"
        val now = Clock.System.now()

        // When
        val note = Note(
            id = id,
            title = title,
            html = html,
            createdAt = now,
            updatedAt = now
        )

        // Then
        assertEquals(id, note.id)
        assertEquals(title, note.title)
        assertEquals(html, note.html)
        assertEquals(now, note.createdAt)
        assertEquals(now, note.updatedAt)
    }

    @Test
    fun `should handle empty title`() {
        // Given
        val note = Note(
            id = "note-123",
            title = "",
            html = "<p>Content</p>",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(note.title)
        assertTrue(note.title.isEmpty())
    }

    @Test
    fun `should handle empty html content`() {
        // Given
        val note = Note(
            id = "note-123",
            title = "Title",
            html = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(note.html)
        assertTrue(note.html.isEmpty())
    }

    @Test
    fun `should handle complex html content`() {
        // Given
        val complexHtml = """
            <div>
                <h1>Main Title</h1>
                <p>This is a paragraph with <strong>bold text</strong> and <em>italic text</em>.</p>
                <ul>
                    <li>Item 1</li>
                    <li>Item 2</li>
                </ul>
                <a href="https://example.com">Link</a>
            </div>
        """.trimIndent()
        
        val note = Note(
            id = "note-123",
            title = "Complex HTML Note",
            html = complexHtml,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(complexHtml, note.html)
        assertTrue(note.html.contains("<h1>"))
        assertTrue(note.html.contains("<p>"))
        assertTrue(note.html.contains("<ul>"))
        assertTrue(note.html.contains("<a href="))
    }

    @Test
    fun `should handle unicode characters in title and content`() {
        // Given
        val unicodeTitle = "テストノート 📝"
        val unicodeHtml = "<p>日本語のコンテンツ 🌸</p>"
        
        val note = Note(
            id = "note-123",
            title = unicodeTitle,
            html = unicodeHtml,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(unicodeTitle, note.title)
        assertEquals(unicodeHtml, note.html)
    }
}