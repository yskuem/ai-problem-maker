package app.yskuem.aimondaimaker.feature.select_alubum_or_camera.di

import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.CameraPermissionViewModel
import app.yskuem.aimondaimaker.feature.select_alubum_or_camera.SelectAlbumOrCameraViewModel
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import org.koin.dsl.module

val selectAlbumOrCameraModule = module {
    factory { (controller: PermissionsController) ->
        CameraPermissionViewModel(permissionsController = controller)
    }
}