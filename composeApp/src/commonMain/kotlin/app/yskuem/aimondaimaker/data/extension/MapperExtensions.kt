package app.yskuem.aimondaimaker.data.extension

import app.yskuem.aimondaimaker.data.api.response.ProblemDto
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.entity.Problem
import app.yskuem.aimondaimaker.domain.entity.Project

fun ProblemDto.toDomain(): Problem = Problem(
    answer      = answer,
    category    = category,
    question    = question,
    choices     = choices.shuffled(),// 正解が1番目にくることが多いので選択肢をシャッフルしておく
    explanation = explanation
)


fun Project.toDTO(): ProjectDto = ProjectDto(
    id          = id,
    createdUserId = createdUserId,
    name        = name,
    createdAt   = createdAt.toString(),
    updatedAt   = updatedAt.toString(),
)