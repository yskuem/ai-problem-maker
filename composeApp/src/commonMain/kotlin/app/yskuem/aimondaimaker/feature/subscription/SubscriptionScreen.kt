package app.yskuem.aimondaimaker.feature.subscription

import PaywallPart
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen


class SubscriptionScreen: Screen {
    @Composable
    override fun Content() {
        PaywallPart()
    }
}