package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di

import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.CameraPermissionViewModel
import dev.icerock.moko.permissions.PermissionsController
import org.koin.dsl.module

val selectAlbumOrCameraModule =
    module {
        factory { (controller: PermissionsController) ->
            CameraPermissionViewModel(permissionsController = controller)
        }
    }
