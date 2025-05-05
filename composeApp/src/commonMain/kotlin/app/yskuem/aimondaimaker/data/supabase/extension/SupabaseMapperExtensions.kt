package app.yskuem.aimondaimaker.data.supabase.extension

import app.yskuem.aimondaimaker.data.supabase.response.QuizSupabaseDto
import app.yskuem.aimondaimaker.domain.entity.Quiz

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