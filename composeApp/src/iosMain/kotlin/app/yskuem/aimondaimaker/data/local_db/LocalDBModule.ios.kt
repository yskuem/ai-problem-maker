package app.yskuem.aimondaimaker.data.local_db

import org.koin.core.module.Module
import org.koin.dsl.module

actual val localDbModule: Module =
    module {
        single<UserDataStore> {
            IosUserDataStore()
        }
    }
