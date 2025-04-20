package app.yskuem.aimondaimaker.data.repository.di

import app.yskuem.aimondaimaker.data.repository.ProblemRepositoryImpl
import app.yskuem.aimondaimaker.domain.data.repository.ProblemRepository
import org.koin.dsl.module

val repositoryModule = module {
     single<ProblemRepository> {
         ProblemRepositoryImpl()
     }
}