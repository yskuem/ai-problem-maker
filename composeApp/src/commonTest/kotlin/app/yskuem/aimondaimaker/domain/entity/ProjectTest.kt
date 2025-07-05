package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ProjectTest {

    @Test
    fun `should create project with all required fields`() {
        // Given
        val id = "project-123"
        val createdUserId = "user-456"
        val name = "Test Project"
        val now = Clock.System.now()

        // When
        val project = Project(
            id = id,
            createdUserId = createdUserId,
            name = name,
            createdAt = now,
            updatedAt = now
        )

        // Then
        assertEquals(id, project.id)
        assertEquals(createdUserId, project.createdUserId)
        assertEquals(name, project.name)
        assertEquals(now, project.createdAt)
        assertEquals(now, project.updatedAt)
    }

    @Test
    fun `should handle empty project name`() {
        // Given
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(project.name)
        assertTrue(project.name.isEmpty())
    }

    @Test
    fun `should handle empty user id`() {
        // Given
        val project = Project(
            id = "project-123",
            createdUserId = "",
            name = "Test Project",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(project.createdUserId)
        assertTrue(project.createdUserId.isEmpty())
    }

    @Test
    fun `should handle empty project id`() {
        // Given
        val project = Project(
            id = "",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(project.id)
        assertTrue(project.id.isEmpty())
    }

    @Test
    fun `should handle unicode characters in project name`() {
        // Given
        val unicodeName = "プロジェクト名 📁"
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = unicodeName,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(unicodeName, project.name)
    }

    @Test
    fun `should handle different created and updated times`() {
        // Given
        val createdAt = Clock.System.now()
        val updatedAt = Clock.System.now()
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        // Then
        assertEquals(createdAt, project.createdAt)
        assertEquals(updatedAt, project.updatedAt)
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val now = Clock.System.now()
        val project1 = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )
        val project2 = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )
        val project3 = Project(
            id = "project-456",
            createdUserId = "user-456",
            name = "Test Project",
            createdAt = now,
            updatedAt = now
        )

        // When & Then
        assertEquals(project1, project2)
        assertTrue(project1 == project2)
        assertTrue(project1 != project3)
    }

    @Test
    fun `should handle long project names`() {
        // Given
        val longName = "This is a very long project name that contains many characters and should be handled properly by the Project entity without any issues"
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = longName,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(longName, project.name)
        assertTrue(project.name.length > 50)
    }

    @Test
    fun `should handle special characters in project name`() {
        // Given
        val specialName = "Project #1 - Test & Development (2024)"
        val project = Project(
            id = "project-123",
            createdUserId = "user-456",
            name = specialName,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(specialName, project.name)
    }
}