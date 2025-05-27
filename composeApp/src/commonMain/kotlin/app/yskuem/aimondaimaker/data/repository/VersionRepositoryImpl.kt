package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.entity.Version
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig

class VersionRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig,
): VersionRepository {
    override suspend fun getCurrentAppVersion(): Version {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
    }

    override suspend fun fetchLastestAppVersion(): Version {
        return Version(
            remoteConfig.getValue("lastest_app_version").asString()
        )
    }

    override suspend fun fetchForcedUpdateMinVersion(): Version {
        return Version(
            remoteConfig.getValue("forced_update_min_version").asString()
        )
    }

}