package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.core.util.getCurrentAppVersionString
import app.yskuem.aimondaimaker.data.remote_config.getLastestVersionKey
import app.yskuem.aimondaimaker.data.remote_config.getRequireMinVersionKey
import app.yskuem.aimondaimaker.data.remote_config.getStoreUrlKey
import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.entity.Version
import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig
import kotlin.time.Duration

class VersionRepositoryImpl(
    private val remoteConfig: FirebaseRemoteConfig,
) : VersionRepository {
    override suspend fun getCurrentAppVersion(): Version =
        Version(
            getCurrentAppVersionString(),
        )

    override suspend fun fetchAndActivate() {
        remoteConfig.fetch(minimumFetchInterval = Duration.ZERO)
        remoteConfig.activate()
    }

    override suspend fun fetchLastestAppVersion(): Version =
        Version(
            remoteConfig
                .getValue(
                    getLastestVersionKey(),
                ).asString(),
        )

    override suspend fun fetchRequireMinVersion(): Version =
        Version(
            remoteConfig
                .getValue(
                    getRequireMinVersionKey(),
                ).asString(),
        )

    override suspend fun fetchStoreUrl(): String =
        remoteConfig
            .getValue(
                getStoreUrlKey(),
            ).asString()
}
