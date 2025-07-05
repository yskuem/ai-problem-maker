package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun `should create user with all required fields`() {
        // Given
        val id = "user-123"
        val name = "John Doe"
        val avatarUrl = "https://example.com/avatar.jpg"
        val now = Clock.System.now()

        // When
        val user = User(
            id = id,
            name = name,
            avatarUrl = avatarUrl,
            createdAt = now,
            updatedAt = now
        )

        // Then
        assertEquals(id, user.id)
        assertEquals(name, user.name)
        assertEquals(avatarUrl, user.avatarUrl)
        assertEquals(now, user.createdAt)
        assertEquals(now, user.updatedAt)
    }

    @Test
    fun `should create initial state user with proper defaults`() {
        // Given
        val id = "user-123"

        // When
        val user = User.initialState(id)

        // Then
        assertEquals(id, user.id)
        assertEquals("", user.name)
        assertEquals("", user.avatarUrl)
        assertNotNull(user.createdAt)
        assertNotNull(user.updatedAt)
        assertTrue(user.createdAt <= Clock.System.now())
        assertTrue(user.updatedAt <= Clock.System.now())
    }

    @Test
    fun `should handle empty user name`() {
        // Given
        val user = User(
            id = "user-123",
            name = "",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(user.name)
        assertTrue(user.name.isEmpty())
    }

    @Test
    fun `should handle empty avatar url`() {
        // Given
        val user = User(
            id = "user-123",
            name = "John Doe",
            avatarUrl = "",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(user.avatarUrl)
        assertTrue(user.avatarUrl.isEmpty())
    }

    @Test
    fun `should handle empty user id`() {
        // Given
        val user = User(
            id = "",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertNotNull(user.id)
        assertTrue(user.id.isEmpty())
    }

    @Test
    fun `should handle unicode characters in user name`() {
        // Given
        val unicodeName = "田中太郎 👨‍💻"
        val user = User(
            id = "user-123",
            name = unicodeName,
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(unicodeName, user.name)
    }

    @Test
    fun `should handle different created and updated times`() {
        // Given
        val createdAt = Clock.System.now()
        val updatedAt = Clock.System.now()
        val user = User(
            id = "user-123",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        // Then
        assertEquals(createdAt, user.createdAt)
        assertEquals(updatedAt, user.updatedAt)
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val now = Clock.System.now()
        val user1 = User(
            id = "user-123",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = now,
            updatedAt = now
        )
        val user2 = User(
            id = "user-123",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = now,
            updatedAt = now
        )
        val user3 = User(
            id = "user-456",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = now,
            updatedAt = now
        )

        // When & Then
        assertEquals(user1, user2)
        assertTrue(user1 == user2)
        assertTrue(user1 != user3)
    }

    @Test
    fun `should handle long user names`() {
        // Given
        val longName = "This is a very long user name that contains many characters and should be handled properly by the User entity without any issues or truncation"
        val user = User(
            id = "user-123",
            name = longName,
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(longName, user.name)
        assertTrue(user.name.length > 50)
    }

    @Test
    fun `should handle various avatar url formats`() {
        // Given
        val httpsUrl = "https://example.com/avatar.jpg"
        val httpUrl = "http://example.com/avatar.png"
        val dataUrl = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEA..."
        
        val users = listOf(
            User("user-1", "User 1", httpsUrl, Clock.System.now(), Clock.System.now()),
            User("user-2", "User 2", httpUrl, Clock.System.now(), Clock.System.now()),
            User("user-3", "User 3", dataUrl, Clock.System.now(), Clock.System.now())
        )

        // Then
        assertEquals(httpsUrl, users[0].avatarUrl)
        assertEquals(httpUrl, users[1].avatarUrl)
        assertEquals(dataUrl, users[2].avatarUrl)
    }

    @Test
    fun `should handle special characters in user name`() {
        // Given
        val specialName = "John O'Connor - Developer (2024)"
        val user = User(
            id = "user-123",
            name = specialName,
            avatarUrl = "https://example.com/avatar.jpg",
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now()
        )

        // Then
        assertEquals(specialName, user.name)
    }

    @Test
    fun `initial state should create user with current timestamp`() {
        // Given
        val id = "user-123"
        val beforeCreation = Clock.System.now()

        // When
        val user = User.initialState(id)
        val afterCreation = Clock.System.now()

        // Then
        assertTrue(user.createdAt >= beforeCreation)
        assertTrue(user.createdAt <= afterCreation)
        assertTrue(user.updatedAt >= beforeCreation)
        assertTrue(user.updatedAt <= afterCreation)
    }
}