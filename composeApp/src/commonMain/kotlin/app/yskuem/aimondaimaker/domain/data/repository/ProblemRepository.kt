package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.data.api.response.ProblemResponse
import app.yskuem.aimondaimaker.domain.entity.Problem

interface ProblemRepository {
    suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Problem>
}