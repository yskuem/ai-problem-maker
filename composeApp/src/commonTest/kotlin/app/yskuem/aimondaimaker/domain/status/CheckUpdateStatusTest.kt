package app.yskuem.aimondaimaker.domain.status

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckUpdateStatusTest {

    @Test
    fun `should contain all expected status values`() {
        // Given
        val expectedStatuses = setOf(
            CheckUpdateStatus.UPDATED_NEEDED,
            CheckUpdateStatus.HAVE_LATEST_APP_VERSION,
            CheckUpdateStatus.NONE
        )

        // When
        val actualStatuses = CheckUpdateStatus.values().toSet()

        // Then
        assertEquals(expectedStatuses.size, actualStatuses.size)
        assertTrue(actualStatuses.containsAll(expectedStatuses))
    }

    @Test
    fun `should have correct enum values`() {
        // Given & When & Then
        assertEquals("UPDATED_NEEDED", CheckUpdateStatus.UPDATED_NEEDED.name)
        assertEquals("HAVE_LATEST_APP_VERSION", CheckUpdateStatus.HAVE_LATEST_APP_VERSION.name)
        assertEquals("NONE", CheckUpdateStatus.NONE.name)
    }

    @Test
    fun `should have correct ordinal values`() {
        // Given & When & Then
        assertEquals(0, CheckUpdateStatus.UPDATED_NEEDED.ordinal)
        assertEquals(1, CheckUpdateStatus.HAVE_LATEST_APP_VERSION.ordinal)
        assertEquals(2, CheckUpdateStatus.NONE.ordinal)
    }

    @Test
    fun `should be able to convert to string`() {
        // Given & When & Then
        assertEquals("UPDATED_NEEDED", CheckUpdateStatus.UPDATED_NEEDED.toString())
        assertEquals("HAVE_LATEST_APP_VERSION", CheckUpdateStatus.HAVE_LATEST_APP_VERSION.toString())
        assertEquals("NONE", CheckUpdateStatus.NONE.toString())
    }

    @Test
    fun `should be able to create from string`() {
        // Given & When & Then
        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, CheckUpdateStatus.valueOf("UPDATED_NEEDED"))
        assertEquals(CheckUpdateStatus.HAVE_LATEST_APP_VERSION, CheckUpdateStatus.valueOf("HAVE_LATEST_APP_VERSION"))
        assertEquals(CheckUpdateStatus.NONE, CheckUpdateStatus.valueOf("NONE"))
    }

    @Test
    fun `should support equality comparison`() {
        // Given
        val status1 = CheckUpdateStatus.UPDATED_NEEDED
        val status2 = CheckUpdateStatus.UPDATED_NEEDED
        val status3 = CheckUpdateStatus.NONE

        // When & Then
        assertEquals(status1, status2)
        assertTrue(status1 == status2)
        assertTrue(status1 != status3)
    }

    @Test
    fun `should support when expression`() {
        // Given
        val testStatuses = listOf(
            CheckUpdateStatus.UPDATED_NEEDED,
            CheckUpdateStatus.HAVE_LATEST_APP_VERSION,
            CheckUpdateStatus.NONE
        )

        // When & Then
        testStatuses.forEach { status ->
            val result = when (status) {
                CheckUpdateStatus.UPDATED_NEEDED -> "Update required"
                CheckUpdateStatus.HAVE_LATEST_APP_VERSION -> "Update available"
                CheckUpdateStatus.NONE -> "No update needed"
            }
            
            assertTrue(result.isNotEmpty())
        }
    }
}