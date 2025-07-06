package app.yskuem.aimondaimaker.core.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class DataUiStateTest {
    @Test
    fun `should create Loading state`() {
        val state = DataUiState.Loading

        assertTrue(state.isLoading)
        assertTrue(state is DataUiState.Loading)
    }

    @Test
    fun `should create Success state with data`() {
        val testData = "test data"
        val state = DataUiState.Success(testData)

        assertFalse(state.isLoading)
        assertTrue(state is DataUiState.Success)
        assertEquals(testData, state.data)
    }

    @Test
    fun `should create Error state with throwable`() {
        val exception = Exception("Test error")
        val state = DataUiState.Error(exception)

        assertFalse(state.isLoading)
        assertTrue(state is DataUiState.Error)
        assertSame(exception, state.throwable)
    }

    @Test
    fun `should handle Success state with different data types`() {
        val intState = DataUiState.Success(42)
        val listState = DataUiState.Success(listOf("a", "b", "c"))
        val booleanState = DataUiState.Success(true)

        assertEquals(42, intState.data)
        assertEquals(listOf("a", "b", "c"), listState.data)
        assertEquals(true, booleanState.data)

        assertFalse(intState.isLoading)
        assertFalse(listState.isLoading)
        assertFalse(booleanState.isLoading)
    }

    @Test
    fun `should handle Success state with null data`() {
        val state = DataUiState.Success(null)

        assertFalse(state.isLoading)
        assertTrue(state is DataUiState.Success)
        assertEquals(null, state.data)
    }

    @Test
    fun `should handle Success state with empty collections`() {
        val emptyListState = DataUiState.Success(emptyList<String>())
        val emptyMapState = DataUiState.Success(emptyMap<String, String>())

        assertTrue(emptyListState.data.isEmpty())
        assertTrue(emptyMapState.data.isEmpty())
        assertFalse(emptyListState.isLoading)
        assertFalse(emptyMapState.isLoading)
    }

    @Test
    fun `should handle Error state with different exception types`() {
        val runtimeException = RuntimeException("Runtime error")
        val illegalArgumentException = IllegalArgumentException("Invalid argument")
        val customException = CustomException("Custom error")

        val state1 = DataUiState.Error(runtimeException)
        val state2 = DataUiState.Error(illegalArgumentException)
        val state3 = DataUiState.Error(customException)

        assertSame(runtimeException, state1.throwable)
        assertSame(illegalArgumentException, state2.throwable)
        assertSame(customException, state3.throwable)

        assertFalse(state1.isLoading)
        assertFalse(state2.isLoading)
        assertFalse(state3.isLoading)
    }

    @Test
    fun `should handle Error state with exception message`() {
        val exception = Exception("Detailed error message")
        val state = DataUiState.Error(exception)

        assertEquals("Detailed error message", state.throwable.message)
    }

    @Test
    fun `should handle Error state with null message exception`() {
        val exception = Exception(null as String?)
        val state = DataUiState.Error(exception)

        assertEquals(null, state.throwable.message)
    }

    @Test
    fun `should maintain singleton behavior for Loading state`() {
        val loading1 = DataUiState.Loading
        val loading2 = DataUiState.Loading

        assertSame(loading1, loading2)
        assertTrue(loading1.isLoading)
        assertTrue(loading2.isLoading)
    }

    @Test
    fun `should support type safety for Success state`() {
        val stringState: DataUiState<String> = DataUiState.Success("test")
        val intState: DataUiState<Int> = DataUiState.Success(123)
        val listState: DataUiState<List<String>> = DataUiState.Success(listOf("a", "b"))

        assertTrue(stringState is DataUiState.Success<String>)
        assertTrue(intState is DataUiState.Success<Int>)
        assertTrue(listState is DataUiState.Success<List<String>>)
    }

    private class CustomException(
        message: String,
    ) : Exception(message)
}
