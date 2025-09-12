package app.yskuem.aimondaimaker.data.supabase.extension

import app.yskuem.aimondaimaker.data.supabase.response.NoteSupabaseDto
import app.yskuem.aimondaimaker.data.supabase.response.QuizInfoDto
import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
import app.yskuem.aimondaimaker.data.supabase.response.UserDto
import app.yskuem.aimondaimaker.domain.entity.Note
import app.yskuem.aimondaimaker.domain.entity.Quiz
import app.yskuem.aimondaimaker.domain.entity.QuizInfo
import app.yskuem.aimondaimaker.domain.entity.User
import kotlin.time.ExperimentalTime

// Quiz
@OptIn(ExperimentalTime::class)
fun Quiz.toDTO(): QuizSupabaseDto =
    QuizSupabaseDto(
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

@OptIn(ExperimentalTime::class)
fun QuizSupabaseDto.toDomain(): Quiz =
    Quiz(
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

// Note
@OptIn(ExperimentalTime::class)
fun Note.toDTO(
    projectId: String,
    createdUserId: String,
): NoteSupabaseDto =
    NoteSupabaseDto(
        id = id,
        title = title,
        html = html,
        projectId = projectId,
        createdUserId = createdUserId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

@OptIn(ExperimentalTime::class)
fun NoteSupabaseDto.toDomain(): Note =
    Note(
        id = id,
        title = title,
        html = html,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

// QuizInfo
@OptIn(ExperimentalTime::class)
fun QuizInfo.toDTO(): QuizInfoDto =
    QuizInfoDto(
        projectId = projectId,
        groupId = groupId,
        createdUserId = createdUserId,
        name = name,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )

@OptIn(ExperimentalTime::class)
fun QuizInfoDto.toDomain(): QuizInfo =
    QuizInfo(
        projectId = projectId,
        groupId = groupId,
        createdUserId = createdUserId,
        name = name,
        updatedAt = updatedAt,
        createdAt = createdAt,
    )

// User
@OptIn(ExperimentalTime::class)
fun UserDto.toDomain(): User =
    User(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

@OptIn(ExperimentalTime::class)
fun User.toDTO(): UserDto =
    UserDto(
        id = id,
        name = name,
        avatarUrl = avatarUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
