package app.yskuem.aimondaimaker.data.local_db

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import platform.Foundation.NSUserDefaults

@OptIn(ExperimentalSettingsApi::class)
class IosUserDataStore : UserDataStore() {
    override val flowSettings =
        NSUserDefaultsSettings(
            NSUserDefaults.standardUserDefaults,
        ).toFlowSettings()
}
