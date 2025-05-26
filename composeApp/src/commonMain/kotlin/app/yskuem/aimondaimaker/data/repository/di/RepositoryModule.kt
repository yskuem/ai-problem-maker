package app.yskuem.aimondaimaker.data.repository.di

import app.yskuem.aimondaimaker.core.util.ContextFactory
import app.yskuem.aimondaimaker.data.repository.AdRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.AuthRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.NoteRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.ProjectRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.QuizRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.UserRepositoryImpl
import app.yskuem.aimondaimaker.domain.data.repository.AdRepository
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
     single<QuizRepository> {
         QuizRepositoryImpl(
             supabaseClientHelper = get(),
         )
     }
    single<AuthRepository> {
        AuthRepositoryImpl(
            supabaseClient = get(),
        )
    }
    single<NoteRepository> {
        NoteRepositoryImpl(
            supabaseClientHelper = get(),
        )
    }
    single<ProjectRepository> {
        ProjectRepositoryImpl(
            authRepository = get(),
            supabaseClientHelper = get(),
        )
    }
    single<UserRepository> {
        UserRepositoryImpl(
            supabaseClientHelper = get(),
            authRepository = get(),
        )
    }
    single<AdRepository> {
        AdRepositoryImpl(
            contextFactory = ContextFactory()
        )
    }
}