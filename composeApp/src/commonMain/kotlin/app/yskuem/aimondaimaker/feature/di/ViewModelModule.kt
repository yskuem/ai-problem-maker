package app.yskuem.aimondaimaker.feature.di

import app.yskuem.aimondaimaker.feature.auth.ui.AuthScreenViewModel
import app.yskuem.aimondaimaker.feature.note.ui.ShowNoteScreenViewModel
import app.yskuem.aimondaimaker.feature.quiz.viewmodel.ShowQuizScreenViewModel
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.SelectAlbumOrCameraViewModel
import app.yskuem.aimondaimaker.feature.select_project.ui.SelectProjectScreenViewModel
import app.yskuem.aimondaimaker.feature.show_project_info.ShowProjectInfoScreenViewModel
import app.yskuem.aimondaimaker.feature.subscription.SubscriptionScreenViewModel
import app.yskuem.aimondaimaker.feature.update_check.UpdateCheckScreenViewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        factory {
            AuthScreenViewModel(
                authRepository = get(),
                userRepository = get(),
            )
        }
        factory {
            ShowQuizScreenViewModel(
                quizRepository = get(),
                authRepository = get(),
                projectRepository = get(),
                crashlytics = get(),
                pdfRepository = get(),
                subscriptionRepository = get(),
            )
        }
        factory {
            ShowNoteScreenViewModel(
                noteRepository = get(),
                authRepository = get(),
                projectRepository = get(),
                crashlytics = get(),
            )
        }
        factory {
            SelectAlbumOrCameraViewModel(
                imagePicker = get(),
            )
        }
        factory {
            SelectProjectScreenViewModel(
                projectRepository = get(),
            )
        }
        factory { (projectId: String) ->
            ShowProjectInfoScreenViewModel(
                quizRepository = get(),
                noteRepository = get(),
                projectId = projectId,
            )
        }
        factory {
            UpdateCheckScreenViewModel(
                checkUpdateUseCase = get(),
                openUrl = get(),
                versionRepository = get(),
            )
        }
        factory {
            SubscriptionScreenViewModel(
                authRepository = get(),
                subscriptionRepository = get(),
            )
        }
    }
