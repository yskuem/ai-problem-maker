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

    val lastReviewRequestTimestampFlow: Flow<Long>
        get() = flowSettings.getLongFlow(KEY_LAST_REVIEW_REQUEST_TIMESTAMP, 0L)

    suspend fun setLastReviewRequestTimestamp(timestamp: Long) {
        flowSettings.putLong(KEY_LAST_REVIEW_REQUEST_TIMESTAMP, timestamp)
    }

    suspend fun getLastReviewRequestTimestamp(): Long = flowSettings.getLong(KEY_LAST_REVIEW_REQUEST_TIMESTAMP, 0L)

    companion object {
        const val KEY_USER_ID = "user_id"
        const val KEY_LAST_REVIEW_REQUEST_TIMESTAMP = "last_review_request_timestamp"
    }
}
