package app.yskuem.aimondaimaker.domain.data.repository

import app.yskuem.aimondaimaker.domain.entity.Note

interface NoteRepository {
    /**
     * ノートを画像から作成
     */
    suspend fun generateFromImage(
        image: ByteArray,
        fileName: String,
        extension: String,
    ): Note

    /**
     * プロジェクトのノートの取得
     */
    suspend fun fetchNotes(projectId: String): List<Note>

    /**
     * プロジェクトのノートをページネーション付きで取得
     */
    suspend fun fetchNotes(projectId: String, limit: Int, offset: Int): List<Note>

    /**
     * ノートの保存
     */
    suspend fun saveNote(
        note: Note,
        projectId: String,
        userId: String,
    )

    /**
     * ノートの削除
     */
    suspend fun deleteNote(noteId: String): Boolean

    /**
     * プロジェクトIDによるノートの削除
     */
    suspend fun deleteNotesByProjectId(projectId: String): Boolean
}
