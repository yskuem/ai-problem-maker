package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AdUseCaseImplTest {

    @Test
    fun `should show interstitial ad when interstitial is enabled`() = runTest {
        // Given
        val mockRepository = MockAdRepository(interstitialEnabled = true)
        val useCase = AdUseCaseImpl(mockRepository)

        // When
        useCase.onInterstitialAdLoaded()

        // Then
        assertTrue(mockRepository.showInterstitialAdCalled)
    }

    @Test
    fun `should not show interstitial ad when interstitial is disabled`() = runTest {
        // Given
        val mockRepository = MockAdRepository(interstitialEnabled = false)
        val useCase = AdUseCaseImpl(mockRepository)

        // When
        useCase.onInterstitialAdLoaded()

        // Then
        assertFalse(mockRepository.showInterstitialAdCalled)
    }

    @Test
    fun `should handle multiple calls correctly`() = runTest {
        // Given
        val mockRepository = MockAdRepository(interstitialEnabled = true)
        val useCase = AdUseCaseImpl(mockRepository)

        // When
        useCase.onInterstitialAdLoaded()
        useCase.onInterstitialAdLoaded()

        // Then
        assertEquals(2, mockRepository.showInterstitialAdCallCount)
    }

    @Test
    fun `should handle enabling and disabling interstitial ads`() = runTest {
        // Given
        val mockRepository = MockAdRepository(interstitialEnabled = false)
        val useCase = AdUseCaseImpl(mockRepository)

        // When - First call when disabled
        useCase.onInterstitialAdLoaded()
        val firstCallCount = mockRepository.showInterstitialAdCallCount

        // Enable ads
        mockRepository.setInterstitialEnabled(true)
        
        // When - Second call when enabled
        useCase.onInterstitialAdLoaded()
        val secondCallCount = mockRepository.showInterstitialAdCallCount

        // Then
        assertEquals(0, firstCallCount)
        assertEquals(1, secondCallCount)
    }

    // Mock implementation for testing
    private class MockAdRepository(
        initialInterstitialEnabled: Boolean = true
    ) : AdRepository {
        
        private val _interstitialEnabled = MutableStateFlow(initialInterstitialEnabled)
        override val interstitialEnabled: StateFlow<Boolean> = _interstitialEnabled
        
        var showInterstitialAdCalled = false
            private set
        
        var showInterstitialAdCallCount = 0
            private set
        
        var loadInterstitialAdsCalled = false
            private set

        override fun loadInterstitialAds() {
            loadInterstitialAdsCalled = true
        }

        override fun showInterstitialAd() {
            showInterstitialAdCalled = true
            showInterstitialAdCallCount++
        }
        
        // Test helper method
        fun setInterstitialEnabled(enabled: Boolean) {
            _interstitialEnabled.value = enabled
        }
    }
}