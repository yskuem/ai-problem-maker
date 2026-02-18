package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.user.UserInfo

class AuthRepositoryImpl(
    private val supabaseClient: SupabaseClient,
) : AuthRepository {
    override suspend fun signInAnonymous() {
        supabaseClient.auth.signInAnonymously()
    }

    override suspend fun getUser(): UserInfo? {
        // 待たないとiOSで常時nullになる
        supabaseClient.auth.awaitInitialization()
        return supabaseClient.auth.currentUserOrNull()
    }

    override suspend fun getUserId(): String {
        val userId = supabaseClient.auth.currentUserOrNull()?.id
        return userId ?: throw IllegalStateException("User is not signed in")
    }

    override suspend fun linkWithGoogle() {
        supabaseClient.auth.linkIdentity(Google)
    }

    override suspend fun linkWithApple() {
        supabaseClient.auth.linkIdentity(Apple)
    }

    override fun isAnonymousUser(): Boolean {
        val user = supabaseClient.auth.currentUserOrNull()
        return user?.identities.isNullOrEmpty()
    }

    override fun getLinkedProviderName(): String? {
        val user = supabaseClient.auth.currentUserOrNull()
        return user?.identities?.firstOrNull()?.provider
    }

    override fun getLinkedEmail(): String? {
        val user = supabaseClient.auth.currentUserOrNull()
        return user?.email
    }
}
