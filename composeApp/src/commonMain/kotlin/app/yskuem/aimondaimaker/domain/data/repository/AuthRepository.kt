package app.yskuem.aimondaimaker.domain.data.repository

import io.github.jan.supabase.auth.user.UserInfo

interface AuthRepository {
    suspend fun signInAnonymous()
    suspend fun getUser(): UserInfo?
    suspend fun getUserId(): String?
}