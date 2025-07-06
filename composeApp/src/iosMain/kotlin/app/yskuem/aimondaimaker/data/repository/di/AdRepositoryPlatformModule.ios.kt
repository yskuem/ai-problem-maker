package app.yskuem.aimondaimaker.data.repository.di

import app.yskuem.aimondaimaker.data.repository.AdRepositoryImpl
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual val adRepositoryPlatformModule: Module =
    module {
        single<AdRepository> {
            AdRepositoryImpl()
        }
    }
