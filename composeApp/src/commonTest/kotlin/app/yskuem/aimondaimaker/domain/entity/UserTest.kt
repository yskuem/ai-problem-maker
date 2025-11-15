package app.yskuem.aimondaimaker.domain.entity

import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun `should create user with valid data`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T00:00:00Z")
        val user = User(
            id = "user-1",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.png",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals("user-1", user.id)
        assertEquals("John Doe", user.name)
        assertEquals("https://example.com/avatar.png", user.avatarUrl)
        assertEquals(createdAt, user.createdAt)
        assertEquals(updatedAt, user.updatedAt)
    }

    @Test
    fun `should create user with empty name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val user = User(
            id = "user-1",
            name = "",
            avatarUrl = "https://example.com/avatar.png",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(user.name.isEmpty())
    }

    @Test
    fun `should create user with empty avatar URL`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val user = User(
            id = "user-1",
            name = "John Doe",
            avatarUrl = "",
            createdAt = now,
            updatedAt = now
        )

        assertTrue(user.avatarUrl.isEmpty())
    }

    @Test
    fun `should create user with Unicode characters in name`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val userName = "田中太郎 👨‍💻"
        val user = User(
            id = "user-1",
            name = userName,
            avatarUrl = "https://example.com/avatar.png",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(userName, user.name)
    }

    @Test
    fun `should create user with different created and updated timestamps`() {
        val createdAt = Instant.parse("2024-01-01T00:00:00Z")
        val updatedAt = Instant.parse("2024-01-02T12:30:00Z")
        val user = User(
            id = "user-1",
            name = "John Doe",
            avatarUrl = "https://example.com/avatar.png",
            createdAt = createdAt,
            updatedAt = updatedAt
        )

        assertEquals(createdAt, user.createdAt)
        assertEquals(updatedAt, user.updatedAt)
        assertTrue(user.updatedAt > user.createdAt)
    }

    @Test
    fun `should create initial state user with provided ID`() {
        val user = User.initialState("user-123")

        assertEquals("user-123", user.id)
        assertTrue(user.name.isEmpty())
        assertTrue(user.avatarUrl.isEmpty())
        assertTrue(user.createdAt.epochSeconds > 0)
        assertTrue(user.updatedAt.epochSeconds > 0)
    }

    @Test
    fun `should create initial state user with current timestamps`() {
        val user = User.initialState("user-123")

        // The timestamps should be close to the current time (within a few seconds)
        val now = kotlinx.datetime.Clock.System.now()
        val timeDifference = now.epochSeconds - user.createdAt.epochSeconds
        assertTrue(timeDifference >= 0 && timeDifference <= 5, "Creation time should be close to current time")
    }

    @Test
    fun `should create initial state user with same created and updated timestamps`() {
        val user = User.initialState("user-123")

        // For initial state, created and updated timestamps should be the same or very close
        val timeDifference = user.updatedAt.epochSeconds - user.createdAt.epochSeconds
        assertTrue(timeDifference >= 0 && timeDifference <= 1, "Created and updated timestamps should be the same or very close")
    }

    @Test
    fun `should create multiple initial state users with different IDs`() {
        val user1 = User.initialState("user-1")
        val user2 = User.initialState("user-2")
        val user3 = User.initialState("user-3")

        assertEquals("user-1", user1.id)
        assertEquals("user-2", user2.id)
        assertEquals("user-3", user3.id)
        
        assertTrue(user1.name.isEmpty())
        assertTrue(user2.name.isEmpty())
        assertTrue(user3.name.isEmpty())
    }

    @Test
    fun `should handle long avatar URLs`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val longAvatarUrl = "https://example.com/very/long/path/to/avatar/image/with/many/subdirectories/and/parameters.png?size=300&format=webp&quality=80"
        val user = User(
            id = "user-1",
            name = "John Doe",
            avatarUrl = longAvatarUrl,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(longAvatarUrl, user.avatarUrl)
    }

    @Test
    fun `should handle special characters in avatar URL`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val avatarUrl = "https://example.com/avatar%20with%20spaces.png?param=value&other=123"
        val user = User(
            id = "user-1",
            name = "John Doe",
            avatarUrl = avatarUrl,
            createdAt = now,
            updatedAt = now
        )

        assertEquals(avatarUrl, user.avatarUrl)
    }

    @Test
    fun `should handle very long user names`() {
        val now = Instant.parse("2024-01-01T00:00:00Z")
        val longName = "This is a very long user name that might be used in some applications and should be handled properly by the system"
        val user = User(
            id = "user-1",
            name = longName,
            avatarUrl = "https://example.com/avatar.png",
            createdAt = now,
            updatedAt = now
        )

        assertEquals(longName, user.name)
    }

    @Test
    fun `should handle UUID-like user IDs`() {
        val uuid = "550e8400-e29b-41d4-a716-446655440000"
        val user = User.initialState(uuid)

        assertEquals(uuid, user.id)
    }
}