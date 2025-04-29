package app.yskuem.aimondaimaker.domain.data.repository

interface AuthRepository {
    suspend fun signInAnonymous()
    suspend fun getUserId(): String?
}