package app.yskuem.aimondaimaker.data.local_db

import android.content.Context
import com.russhwolf.settings.ExperimentalSettingsImplementation
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
actual val localDbModule: Module = module {
    single {
        AndroidUserDataStore(
            context = get()
        )
    }
    single<Context> { androidContext() }
}