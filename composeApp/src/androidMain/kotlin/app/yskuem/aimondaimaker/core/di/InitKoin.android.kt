package app.yskuem.aimondaimaker.core.di

import androidx.activity.ComponentActivity
import app.yskuem.aimondaimaker.MainActivity
import app.yskuem.aimondaimaker.MyApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

actual fun initKoinPlatform(): KoinApplication =
    startKoin {
        androidContext(MyApp.instance)
        modules(
            diModules +
                listOf(
                    module {
                        single<ComponentActivity> { MainActivity.instance }
                    },
                ),
        )
    }
