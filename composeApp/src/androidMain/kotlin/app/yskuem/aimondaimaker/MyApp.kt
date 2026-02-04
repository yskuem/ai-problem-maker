package app.yskuem.aimondaimaker

import android.app.Application
import android.content.Context
import app.yskuem.aimondaimaker.core.di.diModules
import app.yskuem.aimondaimaker.feature.subscription.RevenueCatInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinForAndroid(context = this@MyApp)
        RevenueCatInitializer.configureIfNeeded()
    }

    private fun initKoinForAndroid(context: Context) {
        startKoin {
            androidContext(context)
            modules(
                diModules,
            )
        }
    }
}
