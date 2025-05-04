package app.yskuem.aimondaimaker.data.repository.di

import app.yskuem.aimondaimaker.data.repository.AuthRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.QuizRepositoryImpl
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import org.koin.dsl.module

val repositoryModule = module {
     single<QuizRepository> {
         QuizRepositoryImpl(
             authRepository = get(),
             supabaseClientHelper = get(),
         )
     }
    single <AuthRepository>{
        AuthRepositoryImpl(
            supabaseClient = get(),
        )
    }
}