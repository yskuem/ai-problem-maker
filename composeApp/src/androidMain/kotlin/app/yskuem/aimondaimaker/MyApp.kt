package app.yskuem.aimondaimaker

import android.app.Application
import app.yskuem.aimondaimaker.core.di.initKoin
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        FileKit.init(this)
        initKoin()
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}
