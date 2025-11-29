package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.domain.entity.Quiz

interface PdfRepository {
    suspend fun createQuizPdf(
        quizList: List<Quiz>,
        isColorModel: Boolean
    ): PdfResponse
}