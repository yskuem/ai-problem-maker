package app.yskuem.aimondaimaker.feature.subscription

import PaywallPart
import androidx.compose.runtime.Composable
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel


class SubscriptionScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SubscriptionScreenViewModel>()
        when(val offering = viewModel.uiState.value.offering) {
            is DataUiState.Success -> {
                PaywallPart(
                    packages = offering.data.availablePackages,
                    onPurchase = {},
                    onRestore = {}
                )
            }
            is DataUiState.Error -> {
                ErrorScreen(
                    type = ErrorScreenType.BACK,
                    errorMessage = offering.throwable.message ?: "Unknown error",
                )
            }
            else -> {
                LoadingScreen()
            }
        }
    }
}