package app.yskuem.aimondaimaker.domain.entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.datetime.Instant

class NoteTest {

    @Test
    fun `should create note with valid data`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val note = Note(
            id = "note-1",
            title = "Test Note",
            html = "<p>Test content</p>",
            createdAt = now,
            updatedAt = now
        )

        assertEquals("note-1", note.id)
        assertEquals("Test Note", note.title)
        assertEquals("<p>Test content</p>", note.html)
        assertEquals(now, note.createdAt)
        assertEquals(now, note.updatedAt)
    }

    @Test
    fun `should handle empty title`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val note = Note(
            id = "note-1",
            title = "",
            html = "<p>Test content</p>",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(note.title.isEmpty())
    }

    @Test
    fun `should handle empty html content`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val note = Note(
            id = "note-1",
            title = "Test Note",
            html = "",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(note.html.isEmpty())
    }

    @Test
    fun `should handle HTML content with special characters`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val htmlContent = "<p>Test with &lt;special&gt; characters &amp; symbols</p>"
        val note = Note(
            id = "note-1",
            title = "Test Note",
            html = htmlContent,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(htmlContent, note.html)
    }

    @Test
    fun `should handle Unicode characters in title`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val title = "テストノート 📝"
        val note = Note(
            id = "note-1",
            title = title,
            html = "<p>Test content</p>",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(title, note.title)
    }

    @Test
    fun `should handle different timestamps for created and updated`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T00:00:00Z")
        val note = Note(
            id = "note-1",
            title = "Test Note",
            html = "<p>Test content</p>",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals(createdAt, note.createdAt)
        assertEquals(updatedAt, note.updatedAt)
        assertTrue(note.updatedAt > note.createdAt)
    }
}