package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.MainActivity
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin


actual fun initKoin(): KoinApplication = startKoin {
    androidContext(MainActivity.instance)
    modules(diModules)
}