package app.yskuem.aimondaimaker.data.repository.di

import app.yskuem.aimondaimaker.data.repository.AuthRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.NoteRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.ProjectRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.QuizRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.SharedQuizRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.UserRepositoryImpl
import app.yskuem.aimondaimaker.data.repository.VersionRepositoryImpl
import app.yskuem.aimondaimaker.domain.data.repository.AuthRepository
import app.yskuem.aimondaimaker.domain.data.repository.NoteRepository
import app.yskuem.aimondaimaker.domain.data.repository.ProjectRepository
import app.yskuem.aimondaimaker.domain.data.repository.QuizRepository
import app.yskuem.aimondaimaker.domain.data.repository.SharedQuizRepository
import app.yskuem.aimondaimaker.domain.data.repository.UserRepository
import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.remoteconfig.remoteConfig
import org.koin.dsl.module

val repositoryModule =
    module {
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
                quizRepository = get(),
                noteRepository = get(),
            )
        }
        single<UserRepository> {
            UserRepositoryImpl(
                supabaseClientHelper = get(),
                authRepository = get(),
            )
        }
        single<VersionRepository> {
            VersionRepositoryImpl(
                remoteConfig = Firebase.remoteConfig,
            )
        }
        single<SharedQuizRepository> {
            SharedQuizRepositoryImpl(
                supabaseClientHelper = get(),
            )
        }
    }
