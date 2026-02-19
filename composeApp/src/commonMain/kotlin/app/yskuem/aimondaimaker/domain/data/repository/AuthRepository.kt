package app.yskuem.aimondaimaker.domain.data.repository

import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInAnonymous()

    suspend fun getUser(): UserInfo?

    suspend fun getUserId(): String

    suspend fun linkWithGoogle()

    suspend fun linkWithApple()

    suspend fun signInWithGoogle()

    suspend fun signInWithApple()

    fun isAnonymousUser(): Boolean

    fun getLinkedProviderName(): String?

    fun getLinkedEmail(): String?

    fun observeSessionStatus(): Flow<SessionStatus>
}
