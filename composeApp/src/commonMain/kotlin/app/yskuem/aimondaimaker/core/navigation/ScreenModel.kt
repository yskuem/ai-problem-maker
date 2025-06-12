package app.yskuem.aimondaimaker.core.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus

interface ScreenModel {
    val screenModelScope: CoroutineScope
    fun onDispose() {
        screenModelScope.cancel()
    }
}

open class DefaultScreenModel : ScreenModel {
    override val screenModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
}
