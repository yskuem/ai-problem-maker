package app.yskuem.aimondaimaker.feature.subscription

sealed interface SubscriptionScreenEvent {
    data object Initialize : SubscriptionScreenEvent
}