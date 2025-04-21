package app.yskuem.aimondaimaker.core.di

import app.yskuem.aimondaimaker.core.picker.ImagePicker
import app.yskuem.aimondaimaker.core.picker.ImagePickerImpl
import org.koin.dsl.module

val coreModule = module {
    single<ImagePicker> {
        ImagePickerImpl()
    }
}