package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.response.ProblemResponse
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository

class ProblemRepositoryImpl: ProblemRepository {
    override suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<ProblemResponse> {
        return HttpClient.postWithImage<List<ProblemResponse>>(
            imageBytes = image,
            fileName = fileName,
            extension = extension,
        )
    }
}