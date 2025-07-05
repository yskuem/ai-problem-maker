package app.yskuem.aimondaimaker.domain.entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VersionTest {

    @Test
    fun `should create version with valid version string`() {
        // Given & When
        val version = Version("1.0.0")

        // Then
        assertEquals("1.0.0", version.value)
        assertFalse(version.isEmpty)
    }

    @Test
    fun `should create empty version`() {
        // Given & When
        val version = Version("")

        // Then
        assertEquals("", version.value)
        assertTrue(version.isEmpty)
    }

    @Test
    fun `should validate version format correctly`() {
        // Given & When & Then
        assertTrue(Version.validate("1.0.0"))
        assertTrue(Version.validate("1.2.3"))
        assertTrue(Version.validate("10.20.30"))
        assertTrue(Version.validate("1.0"))
        assertTrue(Version.validate("1"))
        assertTrue(Version.validate(""))
        
        assertFalse(Version.validate("1.0.0-alpha"))
        assertFalse(Version.validate("1.0.0.0"))
        assertFalse(Version.validate("1.0.0a"))
        assertFalse(Version.validate("v1.0.0"))
        assertFalse(Version.validate("1.0.0-SNAPSHOT"))
        assertFalse(Version.validate("1.0.0."))
    }

    @Test
    fun `should throw exception for invalid version format`() {
        // Given & When & Then
        assertFailsWith<IllegalStateException> {
            Version("invalid.version")
        }
        
        assertFailsWith<IllegalStateException> {
            Version("1.0.0-alpha")
        }
        
        assertFailsWith<IllegalStateException> {
            Version("1.0.0.")
        }
    }

    @Test
    fun `should compare versions correctly - equal versions`() {
        // Given
        val version1 = Version("1.0.0")
        val version2 = Version("1.0.0")

        // When & Then
        assertEquals(0, version1.compareTo(version2))
        assertTrue(version1 == version2)
        assertFalse(version1 < version2)
        assertFalse(version1 > version2)
    }

    @Test
    fun `should compare versions correctly - different major versions`() {
        // Given
        val version1 = Version("1.0.0")
        val version2 = Version("2.0.0")

        // When & Then
        assertTrue(version1 < version2)
        assertFalse(version1 > version2)
        assertTrue(version1.compareTo(version2) < 0)
        assertTrue(version2.compareTo(version1) > 0)
    }

    @Test
    fun `should compare versions correctly - different minor versions`() {
        // Given
        val version1 = Version("1.1.0")
        val version2 = Version("1.2.0")

        // When & Then
        assertTrue(version1 < version2)
        assertFalse(version1 > version2)
        assertTrue(version1.compareTo(version2) < 0)
        assertTrue(version2.compareTo(version1) > 0)
    }

    @Test
    fun `should compare versions correctly - different patch versions`() {
        // Given
        val version1 = Version("1.0.1")
        val version2 = Version("1.0.2")

        // When & Then
        assertTrue(version1 < version2)
        assertFalse(version1 > version2)
        assertTrue(version1.compareTo(version2) < 0)
        assertTrue(version2.compareTo(version1) > 0)
    }

    @Test
    fun `should compare versions with different segment counts`() {
        // Given
        val version1 = Version("1.0")
        val version2 = Version("1.0.1")

        // When & Then
        assertTrue(version1 < version2)
        assertFalse(version1 > version2)
        assertTrue(version1.compareTo(version2) < 0)
        assertTrue(version2.compareTo(version1) > 0)
    }

    @Test
    fun `should compare versions with single digit`() {
        // Given
        val version1 = Version("1")
        val version2 = Version("2")

        // When & Then
        assertTrue(version1 < version2)
        assertFalse(version1 > version2)
        assertTrue(version1.compareTo(version2) < 0)
        assertTrue(version2.compareTo(version1) > 0)
    }

    @Test
    fun `should handle empty version in comparison`() {
        // Given
        val emptyVersion = Version("")
        val normalVersion = Version("1.0.0")

        // When & Then
        assertTrue(emptyVersion < normalVersion)
        assertFalse(emptyVersion > normalVersion)
        assertTrue(emptyVersion.compareTo(normalVersion) < 0)
        assertTrue(normalVersion.compareTo(emptyVersion) > 0)
    }

    @Test
    fun `should handle complex version comparisons`() {
        // Given
        val versions = listOf(
            Version(""),
            Version("1"),
            Version("1.0"),
            Version("1.0.0"),
            Version("1.0.1"),
            Version("1.1.0"),
            Version("2.0.0")
        )

        // When
        val sortedVersions = versions.sorted()

        // Then
        assertEquals(versions, sortedVersions)
    }

    @Test
    fun `should handle version with large numbers`() {
        // Given
        val version1 = Version("10.20.30")
        val version2 = Version("9.99.99")

        // When & Then
        assertTrue(version1 > version2)
        assertFalse(version1 < version2)
        assertTrue(version1.compareTo(version2) > 0)
    }

    @Test
    fun `should handle version comparison with zero padding`() {
        // Given
        val version1 = Version("1.0.0")
        val version2 = Version("1.0.0")

        // When & Then
        assertEquals(0, version1.compareTo(version2))
        assertTrue(version1 == version2)
    }

    @Test
    fun `should support equality correctly`() {
        // Given
        val version1 = Version("1.0.0")
        val version2 = Version("1.0.0")
        val version3 = Version("1.0.1")

        // When & Then
        assertEquals(version1, version2)
        assertTrue(version1 == version2)
        assertFalse(version1 == version3)
        assertTrue(version1 != version3)
    }
}