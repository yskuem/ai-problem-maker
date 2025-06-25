package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.data.picker.ImagePicker
import app.yskuem.aimondaimaker.data.picker.ImagePickerImpl
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import org.koin.dsl.module

val coreModule =
    module {
        single<ImagePicker> {
            ImagePickerImpl()
        }
        single {
            Firebase.crashlytics
        }
    }
