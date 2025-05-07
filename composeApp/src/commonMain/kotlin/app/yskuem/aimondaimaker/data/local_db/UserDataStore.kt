package app.yskuem.aimondaimaker.data.local_db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalSettingsApi::class)
abstract class UserDataStore {
    protected abstract val flowSettings: FlowSettings

    val userIdFlow: Flow<String>
        get() = flowSettings.getStringFlow(KEY_USER_ID, "")

    suspend fun setUserId(userId: String) {
        flowSettings.putString(KEY_USER_ID, userId)
    }

    companion object {
        const val KEY_USER_ID = "user_token"
    }
}