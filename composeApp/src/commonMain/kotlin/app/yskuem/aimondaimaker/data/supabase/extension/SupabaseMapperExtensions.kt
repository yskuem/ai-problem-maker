package app.yskuem.aimondaimaker.data.supabase.extension

import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
import app.yskuem.aimondaimaker.data.supabase.response.UserDto
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.User

fun Quiz.toDTO(): QuizSupabaseDto = QuizSupabaseDto(
    id = id,
    answer = answer,
    title = title,
    groupId = groupId,
    question = question,
    choices = choices,
    explanation = explanation,
    createdAt = createdAt,
    updatedAt = updatedAt,
    projectId = projectId,
    createdUserId = createdUserId,
)

fun UserDto.toDomain(): User = User(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun User.toDTO(): UserDto = UserDto(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
)