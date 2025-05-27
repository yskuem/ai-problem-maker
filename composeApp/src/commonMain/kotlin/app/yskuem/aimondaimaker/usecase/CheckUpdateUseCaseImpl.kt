package app.yskuem.aimondaimaker.usecase

import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase

class CheckUpdateUseCaseImpl(
    private val versionRepository: VersionRepository,
): CheckUpdateUseCase {
    init {

    }
}