package app.yskuem.aimondaimaker.data.api.extension

import app.yskuem.aimondaimaker.data.api.response.ProblemDto
import app.yskuem.aimondaimaker.domain.entity.Problem

fun ProblemDto.toDomain(): Problem = Problem(
    answer      = answer,
    category    = category,
    question    = question,
    choices     = choices,
    explanation = explanation
)