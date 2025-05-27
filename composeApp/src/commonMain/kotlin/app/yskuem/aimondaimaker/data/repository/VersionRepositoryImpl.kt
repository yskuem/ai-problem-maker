package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.core.util.getCurrentAppVersionString
import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.entity.Version
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig

class VersionRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig,
): VersionRepository {
    override suspend fun getCurrentAppVersion(): Version {
        return Version(
            getCurrentAppVersionString()
        )
    }

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }

    override suspend fun fetchLastestAppVersion(): Version {
        return Version(
            remoteConfig.getValue("lastest_app_version").asString()
        )
    }

    override suspend fun fetchRequireMinVersion(): Version {
        return Version(
            remoteConfig.getValue("require_min_version").asString()
        )
    }

}