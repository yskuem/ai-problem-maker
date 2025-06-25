package app.yskuem.aimondaimaker.data.local_db

import com.russhwolf.settings.ExperimentalSettingsImplementation
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
actual val localDbModule: Module =
    module {
        single<UserDataStore> {
            AndroidUserDataStore(
                context = get(),
            )
        }
    }
