package app.yskuem.aimondaimaker

import android.app.Application
import android.content.Context
import app.yskuem.aimondaimaker.core.di.diModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoinForAndroid(context = this@MyApp)
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
