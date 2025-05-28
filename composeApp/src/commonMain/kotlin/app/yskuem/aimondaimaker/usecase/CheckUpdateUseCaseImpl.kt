package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase

class CheckUpdateUseCaseImpl(
    private val versionRepository: VersionRepository,
): CheckUpdateUseCase {
    override suspend fun checkUpdate(): CheckUpdateStatus {
        versionRepository.fetchAndActivate()
        val currentVersion = versionRepository.getCurrentAppVersion()
        println(currentVersion)
        val latestVersion = versionRepository.fetchLastestAppVersion()
        val requireMinVersion = versionRepository.fetchRequireMinVersion()
        if(currentVersion < requireMinVersion) {
            return CheckUpdateStatus.UPDATED_NEEDED
        }
        if(currentVersion < latestVersion) {
            return CheckUpdateStatus.HAVE_LATEST_APP_VERSION
        }
        return CheckUpdateStatus.NONE
    }

}