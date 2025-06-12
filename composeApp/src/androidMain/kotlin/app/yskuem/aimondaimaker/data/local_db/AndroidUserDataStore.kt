package app.yskuem.aimondaimaker.data.local_db

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.datastore.DataStoreSettings

@OptIn(ExperimentalSettingsApi::class)
@ExperimentalSettingsImplementation
class AndroidUserDataStore(
    context: Context,
) : UserDataStore() {
    private val dataStoreSettings = DataStoreSettings(context.dataStore)

    override val flowSettings = dataStoreSettings
}

private val Context.dataStore by preferencesDataStore(
    name = "preferences",
)
