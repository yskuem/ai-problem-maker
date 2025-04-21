package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.extension.toDomain
import app.yskuem.aimondaimaker.data.api.response.ProblemResponse
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository
import app.yskuem.aimondaimaker.domain.entity.Problem

class ProblemRepositoryImpl: ProblemRepository {
    override suspend fun fetchFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): List<Problem> {
        val response = HttpClient.postWithImage<List<ProblemResponse>>(
            imageBytes = image,
            fileName = fileName,
            extension = extension,
        )
        if(response.isEmpty()) {
            throw IllegalStateException("Response is empty")
        }
        return response.first().args.questions.map { it.toDomain() }
    }
}