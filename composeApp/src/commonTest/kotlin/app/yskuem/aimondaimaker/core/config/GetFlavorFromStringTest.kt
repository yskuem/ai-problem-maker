package app.yskuem.aimondaimaker.core.config

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetFlavorFromStringTest {
    @Test
    fun testValidFlavors() {
        assertEquals(Flavor.DEV, getFlavorFromString("dev"))
        assertEquals(Flavor.STAGING, getFlavorFromString("staging"))
        assertEquals(Flavor.PROD, getFlavorFromString("prod"))
    }

    @Test
    fun testInvalidFlavor() {
        assertFailsWith<Throwable> { getFlavorFromString("invalid") }
    }
}
