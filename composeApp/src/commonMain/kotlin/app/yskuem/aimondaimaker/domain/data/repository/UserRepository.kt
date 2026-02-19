package app.yskuem.aimondaimaker.domain.data.repository

interface UserRepository {
    suspend fun saveUser()
    suspend fun existsUser(userId: String): Boolean
}
