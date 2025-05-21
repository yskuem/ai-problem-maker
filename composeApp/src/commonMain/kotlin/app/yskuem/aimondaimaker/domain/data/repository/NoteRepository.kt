package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Note

interface NoteRepository {
    suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): Note

//    suspend fun saveNoteInfo(
//        projectId: String,
//        userId: String,
//        noteId: String,
//        quizTitle: String,
//    )
//
//    suspend fun fetchAnsweredNoteList(
//        projectId: String,
//    ): List<Quiz>
//
//    suspend fun saveQuiz(
//        quiz: Quiz,
//        projectId: String,
//        userId: String,
//    )
//
//    suspend fun fetchQuizInfoList(userId: String): List<QuizInfo>
}