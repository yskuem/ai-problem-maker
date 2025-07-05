package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.entity.Version
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckUpdateUseCaseImplTest {

    @Test
    fun `should return UPDATED_NEEDED when current version is less than required minimum`() {
        // Given
        val mockRepository = MockVersionRepository(
            currentVersion = Version("1.0.0"),
            latestVersion = Version("2.0.0"),
            requireMinVersion = Version("1.5.0")
        )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        // When
        val result = runTest { useCase.checkUpdate() }

        // Then
        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, result)
    }

    @Test
    fun `should return HAVE_LATEST_APP_VERSION when current version is less than latest but meets minimum`() {
        // Given
        val mockRepository = MockVersionRepository(
            currentVersion = Version("1.5.0"),
            latestVersion = Version("2.0.0"),
            requireMinVersion = Version("1.0.0")
        )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        // When
        val result = runTest { useCase.checkUpdate() }

        // Then
        assertEquals(CheckUpdateStatus.HAVE_LATEST_APP_VERSION, result)
    }

    @Test
    fun `should return NONE when current version is equal to latest version`() {
        // Given
        val mockRepository = MockVersionRepository(
            currentVersion = Version("2.0.0"),
            latestVersion = Version("2.0.0"),
            requireMinVersion = Version("1.0.0")
        )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        // When
        val result = runTest { useCase.checkUpdate() }

        // Then
        assertEquals(CheckUpdateStatus.NONE, result)
    }

    @Test
    fun `should return NONE when current version is greater than latest version`() {
        // Given
        val mockRepository = MockVersionRepository(
            currentVersion = Version("2.1.0"),
            latestVersion = Version("2.0.0"),
            requireMinVersion = Version("1.0.0")
        )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        // When
        val result = runTest { useCase.checkUpdate() }

        // Then
        assertEquals(CheckUpdateStatus.NONE, result)
    }

    @Test
    fun `should call fetchAndActivate on repository`() {
        // Given
        val mockRepository = MockVersionRepository(
            currentVersion = Version("1.0.0"),
            latestVersion = Version("1.0.0"),
            requireMinVersion = Version("1.0.0")
        )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        // When
        runTest { useCase.checkUpdate() }

        // Then
        assertEquals(1, mockRepository.fetchAndActivateCallCount)
    }

    // Mock implementation for testing
    private class MockVersionRepository(
        private val currentVersion: Version,
        private val latestVersion: Version,
        private val requireMinVersion: Version
    ) : VersionRepository {
        
        var fetchAndActivateCallCount = 0
            private set

        override suspend fun fetchAndActivate() {
            fetchAndActivateCallCount++
        }

        override suspend fun getCurrentAppVersion(): Version = currentVersion
        override suspend fun fetchLastestAppVersion(): Version = latestVersion
        override suspend fun fetchRequireMinVersion(): Version = requireMinVersion
        override suspend fun fetchStoreUrl(): String = "https://example.com/store"
    }

    // Helper function to run suspending functions in tests
    private fun <T> runTest(block: suspend () -> T): T {
        return kotlinx.coroutines.test.runTest { block() }
    }
}