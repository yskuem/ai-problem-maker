package app.yskuem.aimondaimaker.data.extension

import app.yskuem.aimondaimaker.data.api.response.QuizDto
import app.yskuem.aimondaimaker.data.supabase.response.ProjectDto
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.Project
import kotlinx.datetime.Clock


fun QuizDto.toDomain(): Quiz = Quiz(
    id = id,
    answer = answer,
    title = title,
    groupId = groupId,
    question = question,
    choices = choices.shuffled(),// 正解が1番目にくることが多いので選択肢をシャッフルしておく
    explanation = explanation,
    createdAt = Clock.System.now(),
    updatedAt = Clock.System.now(),
)


fun Project.toDTO(): ProjectDto = ProjectDto(
    id = id,
    createdUserId = createdUserId,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)


fun ProjectDto.toDomain(): Project = Project(
    id = id,
    createdUserId = createdUserId,
    name = name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)