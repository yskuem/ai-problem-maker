package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProjectTest {

    @Test
    fun `should create project with valid data`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T00:00:00Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "Test Project",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals("project-1", project.id)
        assertEquals("user-1", project.createdUserId)
        assertEquals("Test Project", project.name)
        assertEquals(createdAt, project.createdAt)
        assertEquals(updatedAt, project.updatedAt)
    }

    @Test
    fun `should handle empty project name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(project.name.isEmpty())
    }

    @Test
    fun `should handle Japanese project name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val projectName = "プロジェクト名 📚"
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = projectName,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(projectName, project.name)
    }

    @Test
    fun `should handle long project name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val longName = "This is a very long project name that might contain detailed description of what the project is about and its main goals"
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = longName,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(longName, project.name)
    }

    @Test
    fun `should handle special characters in project name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val specialName = "Project with special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>?,./"
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = specialName,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(specialName, project.name)
    }

    @Test
    fun `should handle different created and updated timestamps`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-03T15:30:45Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "Test Project",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals(createdAt, project.createdAt)
        assertEquals(updatedAt, project.updatedAt)
        assertTrue(project.updatedAt > project.createdAt)
    }

    @Test
    fun `should handle same created and updated timestamps`() {
        val timestamp = Instant.parse("2024-01-01T12:00:00Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "Test Project",
            createdAt = timestamp,
            updatedAt = timestamp
        )

        assertEquals(timestamp, project.createdAt)
        assertEquals(timestamp, project.updatedAt)
        assertEquals(project.createdAt, project.updatedAt)
    }

    @Test
    fun `should handle UUID-like IDs`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val projectId = "550e8400-e29b-41d4-a716-446655440000"
        val userId = "123e4567-e89b-12d3-a456-426614174000"
        val project = Project(
            id = projectId,
            createdUserId = userId,
            name = "UUID Project",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(projectId, project.id)
        assertEquals(userId, project.createdUserId)
    }

    @Test
    fun `should handle empty user ID`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val project = Project(
            id = "project-1",
            createdUserId = "",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(project.createdUserId.isEmpty())
    }

    @Test
    fun `should handle numeric string IDs`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val project = Project(
            id = "12345",
            createdUserId = "67890",
            name = "Numeric ID Project",
            createdAt = now,
            updatedAt = now
        )

        assertEquals("12345", project.id)
        assertEquals("67890", project.createdUserId)
    }

    @Test
    fun `should handle project name with line breaks`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val nameWithLineBreaks = "Multi-line\nProject\nName"
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = nameWithLineBreaks,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(nameWithLineBreaks, project.name)
        assertTrue(project.name.contains("\n"))
    }

    @Test
    fun `should handle project name with tabs and spaces`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val nameWithWhitespace = "\t  Project with\ttabs and  spaces  \t"
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = nameWithWhitespace,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(nameWithWhitespace, project.name)
    }

    @Test
    fun `should preserve exact timestamp precision`() {
        val createdAt = Instant.parse("2024-01-01T12:34:56.789Z")
        val updatedAt = Instant.parse("2024-01-01T12:34:56.790Z")
        val project = Project(
            id = "project-1",
            createdUserId = "user-1",
            name = "Precision Test",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals(createdAt, project.createdAt)
        assertEquals(updatedAt, project.updatedAt)
        assertEquals(789, createdAt.nanosecondsOfSecond / 1_000_000)
        assertEquals(790, updatedAt.nanosecondsOfSecond / 1_000_000)
    }
}