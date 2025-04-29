package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient
): AuthRepository {
    override suspend fun signInAnonymous() {
        supabaseClient.auth.signInAnonymously()
    }

    override suspend fun getUser(): UserInfo? {
        return supabaseClient.auth.currentUserOrNull()
    }

    override suspend fun getUserId(): String? {
        return supabaseClient.auth.currentUserOrNull()?.id
    }
}