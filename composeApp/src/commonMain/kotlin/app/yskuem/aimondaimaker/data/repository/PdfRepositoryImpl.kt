package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.api.HttpClient
import app.yskuem.aimondaimaker.data.api.request.PdfRequestDto
import app.yskuem.aimondaimaker.data.api.request.QuestionDto
import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.domain.data.repository.PdfRepository
import app.yskuem.aimondaimaker.domain.entity.Quiz

class PdfRepositoryImpl: PdfRepository {

    override suspend fun createQuizPdf(
        quizList: List<Quiz>,
        isColorModel: Boolean
    ): PdfResponse =
        HttpClient.postForGeneratePdf<PdfRequestDto>(
            requestModel = PdfRequestDto(
                title = quizList.first().title,
                questions = quizList.map {
                    QuestionDto(
                        question = it.question,
                        choices = it.choices,
                        answer = it.answer,
                        explanation = it.explanation,
                    )
                },
                colorMode = true,
            ),
        )
}