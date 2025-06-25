package app.yskuem.aimondaimaker.domain.usecase

import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus

interface CheckUpdateUseCase {
    suspend fun checkUpdate(): CheckUpdateStatus
}
