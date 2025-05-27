package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Version

interface VersionRepository {
    suspend fun getCurrentAppVersion(): Version
    suspend fun fetchAndActivate()
    suspend fun fetchLastestAppVersion(): Version

    suspend fun fetchForcedUpdateMinVersion(): Version
}