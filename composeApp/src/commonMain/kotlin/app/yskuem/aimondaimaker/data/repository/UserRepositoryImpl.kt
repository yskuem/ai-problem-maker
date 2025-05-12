package app.yskuem.aimondaimaker.data.repository

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.SupabaseTableName
import app.yskuem.aimondaimaker.data.supabase.extension.toDTO
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import app.yskuem.aimondaimaker.domain.entity.User

class UserRepositoryImpl(
    private val supabaseClientHelper: SupabaseClientHelper,
    private val authRepository: AuthRepository,
) : UserRepository {
    override suspend fun saveUser() {
        supabaseClientHelper.addItem(
            tableName = SupabaseTableName.User.NAME,
            item = User.initialState(
                id = authRepository.getUserId()
            ).toDTO()
        )
    }
}