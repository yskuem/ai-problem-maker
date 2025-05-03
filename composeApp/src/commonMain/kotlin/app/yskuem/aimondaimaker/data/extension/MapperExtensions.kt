package app.yskuem.aimondaimaker.data.extension

import app.yskuem.aimondaimaker.data.api.response.ProblemDto
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.entity.Problem
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Clock


fun ProblemDto.toDomain(): Problem = Problem(
    answer      = answer,
    category    = category,
    question    = question,
    choices     = choices.shuffled(),// 正解が1番目にくることが多いので選択肢をシャッフルしておく
    explanation = explanation,
    createdAt = Clock.System.now(),
    updatedAt = Clock.System.now(),
)


fun Project.toDTO(): ProjectDto = ProjectDto(
    id          = id,
    createdUserId = createdUserId,
    name        = name,
    createdAt   = createdAt.toString(),
    updatedAt   = updatedAt.toString(),
)