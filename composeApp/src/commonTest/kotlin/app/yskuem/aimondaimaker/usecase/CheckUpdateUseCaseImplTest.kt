package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class CheckUpdateUseCaseImplTest {
    @Test
    fun `should return UPDATED_NEEDED when current version is less than required minimum`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 1,
                latestVersion = 3,
                requiredMinVersion = 2,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        val result =
            kotlinx.coroutines.test.runTest {
                useCase.checkUpdate()
            }

        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, result)
    }

    @Test
    fun `should return HAVE_LATEST_APP_VERSION when current version is less than latest but meets minimum`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 2,
                latestVersion = 3,
                requiredMinVersion = 1,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        val result =
            kotlinx.coroutines.test.runTest {
                useCase.checkUpdate()
            }

        assertEquals(CheckUpdateStatus.HAVE_LATEST_APP_VERSION, result)
    }

    @Test
    fun `should return NONE when current version is equal to latest version`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 3,
                latestVersion = 3,
                requiredMinVersion = 1,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        val result =
            kotlinx.coroutines.test.runTest {
                useCase.checkUpdate()
            }

        assertEquals(CheckUpdateStatus.NONE, result)
    }

    @Test
    fun `should return NONE when current version is greater than latest version`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 4,
                latestVersion = 3,
                requiredMinVersion = 1,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        val result =
            kotlinx.coroutines.test.runTest {
                useCase.checkUpdate()
            }

        assertEquals(CheckUpdateStatus.NONE, result)
    }

    @Test
    fun `should return UPDATED_NEEDED when current version equals required minimum but less than latest`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 2,
                latestVersion = 3,
                requiredMinVersion = 3,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        val result =
            kotlinx.coroutines.test.runTest {
                useCase.checkUpdate()
            }

        assertEquals(CheckUpdateStatus.UPDATED_NEEDED, result)
    }

    @Test
    fun `should call fetchAndActivate on repository`() {
        val mockRepository =
            MockVersionRepository(
                currentVersion = 2,
                latestVersion = 3,
                requiredMinVersion = 1,
            )
        val useCase = CheckUpdateUseCaseImpl(mockRepository)

        kotlinx.coroutines.test.runTest {
            useCase.checkUpdate()
        }

        assertEquals(1, mockRepository.fetchAndActivateCallCount)
    }

    private class MockVersionRepository(
        private val currentVersion: Int,
        private val latestVersion: Int,
        private val requiredMinVersion: Int,
    ) : VersionRepository {
        var fetchAndActivateCallCount = 0
            private set

        override suspend fun fetchAndActivate() {
            fetchAndActivateCallCount++
        }

        override suspend fun getCurrentAppVersion(): Int = currentVersion

        override suspend fun fetchLastestAppVersion(): Int = latestVersion

        override suspend fun fetchRequireMinVersion(): Int = requiredMinVersion
    }
}
