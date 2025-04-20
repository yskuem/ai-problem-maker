package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.data.api.response.ProblemResponse

interface ProblemRepository {
    suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<ProblemResponse>
}