package app.yskuem.aimondaimaker.domain.status

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CheckUpdateStatusTest {

    @Test
    fun `should have UPDATED_NEEDED status`() {
        val status = CheckUpdateStatus.UPDATED_NEEDED
        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, status)
    }

    @Test
    fun `should have HAVE_LATEST_APP_VERSION status`() {
        val status = CheckUpdateStatus.HAVE_LATEST_APP_VERSION
        assertEquals(CheckUpdateStatus.HAVE_LATEST_APP_VERSION, status)
    }

    @Test
    fun `should have NONE status`() {
        val status = CheckUpdateStatus.NONE
        assertEquals(CheckUpdateStatus.NONE, status)
    }

    @Test
    fun `should have exactly three values`() {
        val values = CheckUpdateStatus.values()
        assertEquals(3, values.size)
    }

    @Test
    fun `should contain all expected values`() {
        val values = CheckUpdateStatus.values()
        
        assertTrue(values.contains(CheckUpdateStatus.UPDATED_NEEDED))
        assertTrue(values.contains(CheckUpdateStatus.HAVE_LATEST_APP_VERSION))
        assertTrue(values.contains(CheckUpdateStatus.NONE))
    }

    @Test
    fun `should have correct ordinal values`() {
        assertEquals(0, CheckUpdateStatus.UPDATED_NEEDED.ordinal)
        assertEquals(1, CheckUpdateStatus.HAVE_LATEST_APP_VERSION.ordinal)
        assertEquals(2, CheckUpdateStatus.NONE.ordinal)
    }

    @Test
    fun `should have correct names`() {
        assertEquals("UPDATED_NEEDED", CheckUpdateStatus.UPDATED_NEEDED.name)
        assertEquals("HAVE_LATEST_APP_VERSION", CheckUpdateStatus.HAVE_LATEST_APP_VERSION.name)
        assertEquals("NONE", CheckUpdateStatus.NONE.name)
    }

    @Test
    fun `should create from string name`() {
        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, CheckUpdateStatus.valueOf("UPDATED_NEEDED"))
        assertEquals(CheckUpdateStatus.HAVE_LATEST_APP_VERSION, CheckUpdateStatus.valueOf("HAVE_LATEST_APP_VERSION"))
        assertEquals(CheckUpdateStatus.NONE, CheckUpdateStatus.valueOf("NONE"))
    }

    @Test
    fun `should have meaningful toString representation`() {
        assertEquals("UPDATED_NEEDED", CheckUpdateStatus.UPDATED_NEEDED.toString())
        assertEquals("HAVE_LATEST_APP_VERSION", CheckUpdateStatus.HAVE_LATEST_APP_VERSION.toString())
        assertEquals("NONE", CheckUpdateStatus.NONE.toString())
    }

    @Test
    fun `should support equality comparison`() {
        val status1 = CheckUpdateStatus.UPDATED_NEEDED
        val status2 = CheckUpdateStatus.UPDATED_NEEDED
        val status3 = CheckUpdateStatus.NONE
        
        assertEquals(status1, status2)
        assertTrue(status1 == status2)
        assertTrue(status1 != status3)
    }

    @Test
    fun `should support when expression`() {
        val status = CheckUpdateStatus.UPDATED_NEEDED
        
        val result = when (status) {
            CheckUpdateStatus.UPDATED_NEEDED -> "Update required"
            CheckUpdateStatus.HAVE_LATEST_APP_VERSION -> "Update available"
            CheckUpdateStatus.NONE -> "Up to date"
        }
        
        assertEquals("Update required", result)
    }

    @Test
    fun `should handle all cases in when expression`() {
        val results = CheckUpdateStatus.values().map { status ->
            when (status) {
                CheckUpdateStatus.UPDATED_NEEDED -> "critical"
                CheckUpdateStatus.HAVE_LATEST_APP_VERSION -> "optional"
                CheckUpdateStatus.NONE -> "current"
            }
        }
        
        assertEquals(listOf("critical", "optional", "current"), results)
    }
}