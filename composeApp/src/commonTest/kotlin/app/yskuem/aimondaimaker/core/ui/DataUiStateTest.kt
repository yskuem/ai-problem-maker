package app.yskuem.aimondaimaker.core.ui

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DataUiStateTest {
    @Test
    fun testIsLoading() {
        assertTrue(DataUiState.Loading.isLoading)
        assertFalse(DataUiState.Success(Unit).isLoading)
        assertFalse(DataUiState.Error(Throwable()).isLoading)
    }
}
