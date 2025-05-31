package app.yskuem.aimondaimaker.feature.update_check

import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.util.OpenUrl
import app.yskuem.aimondaimaker.domain.data.repository.VersionRepository
import app.yskuem.aimondaimaker.domain.status.CheckUpdateStatus
import app.yskuem.aimondaimaker.domain.usecase.CheckUpdateUseCase
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UpdateCheckScreenViewModel(
    private val checkUpdateUseCase: CheckUpdateUseCase,
    private val openUrl: OpenUrl,
    private val versionRepository: VersionRepository,
): ScreenModel {
    private val _updateStatus = MutableStateFlow<DataUiState<CheckUpdateStatus>>(
        DataUiState.Loading
    )
    val updateState = _updateStatus.asStateFlow()


    fun checkUpdate() {
        screenModelScope.launch {
            val res = runCatching {
                checkUpdateUseCase.checkUpdate()
            }
            res.onSuccess {
                _updateStatus.emit(DataUiState.Success(it))
            }
            .onFailure {
                println(it)
            }
        }
    }

    fun openStorePage() {
        screenModelScope.launch {
            openUrl.handle(
                url = versionRepository.fetchStoreUrl()
            )
        }
    }
}