package app.yskuem.aimondaimaker.feature.subscription

import PaywallPart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.yskuem.aimondaimaker.core.ui.DataUiState
import app.yskuem.aimondaimaker.core.ui.ErrorScreen
import app.yskuem.aimondaimaker.core.ui.ErrorScreenType
import app.yskuem.aimondaimaker.core.ui.LoadingScreen
import app.yskuem.aimondaimaker.core.ui.combineDataUiStates
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel


class SubscriptionScreen: Screen {
    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<SubscriptionScreenViewModel>()
        val uiState = viewModel.uiState.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.onEvent(SubscriptionScreenEvent.Initialize)
        }
        val combineState by remember(
            uiState.value.offering,
            uiState.value.isSubscribed
        ) {
            derivedStateOf {
                combineDataUiStates(
                    state1 = uiState.value.offering,
                    state2 = uiState.value.isSubscribed,
                    transform = { offering , isSubscribed ->
                        Pair(offering, isSubscribed)
                    }
                )
            }
        }

        when(val state = combineState) {
            is DataUiState.Success -> {
                PaywallPart(
                    packages = state.data.first.availablePackages,
                    onPurchase = {
                        viewModel.onEvent(
                            SubscriptionScreenEvent.PurchaseSubscription(
                                purchasePackage = it
                            )
                        )
                    },
                    onRestore = {
                        viewModel.onEvent(SubscriptionScreenEvent.RestorePurchase)
                    },
                    isSubscribed = state.data.second,
                    isProcessing = uiState.value.isProcessing,
                )
            }
            is DataUiState.Error -> {
                ErrorScreen(
                    type = ErrorScreenType.BACK,
                    errorMessage = state.throwable.message ?: "Unknown error",
                )
            }
            else -> {
                LoadingScreen()
            }
        }
    }
}