package app.yskuem.aimondaimaker.data.supabase.extension

import app.yskuem.aimondaimaker.data.supabase.response.QuizInfoDto
import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
import app.yskuem.aimondaimaker.data.supabase.response.UserDto
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
import app.yskuem.aimondaimaker.domain.entity.User


// Quiz
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

fun QuizSupabaseDto.toDomain(): Quiz = Quiz(
    id = id,
    answer = answer,
    question = question,
    choices = choices,
    explanation = explanation,
    projectId = projectId,
    createdUserId = createdUserId,
    groupId = groupId,
    title = title,
    createdAt = createdAt,
    updatedAt = updatedAt,
)


// QuizInfo
fun QuizInfo.toDTO(): QuizInfoDto = QuizInfoDto(
    projectId = projectId,
    groupId = groupId,
    createdUserId = createdUserId,
    name = name,
    updatedAt = updatedAt,
    createdAt = createdAt,
)

fun QuizInfoDto.toDomain(): QuizInfo = QuizInfo(
    projectId = projectId,
    groupId = groupId,
    createdUserId = createdUserId,
    name = name,
    updatedAt = updatedAt,
    createdAt = createdAt,
)


// User
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