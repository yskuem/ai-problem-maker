package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.data.picker.ImagePicker
import app.yskuem.aimondaimaker.data.picker.ImagePickerImpl
import org.koin.dsl.module

val coreModule =
    module {
        single<ImagePicker> {
            ImagePickerImpl()
        }
    }
