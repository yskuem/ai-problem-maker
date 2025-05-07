package app.yskuem.aimondaimaker.data.local_db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings

@OptIn(ExperimentalSettingsApi::class)
abstract class UserDataStore {
    protected abstract val flowSettings: FlowSettings
}