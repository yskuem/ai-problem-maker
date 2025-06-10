package cafe.adriel.voyager.core.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface ScreenModel

private object ScreenModelScopeStore {
    val scopes = mutableMapOf<ScreenModel, CoroutineScope>()
}

val ScreenModel.screenModelScope: CoroutineScope
    get() = ScreenModelScopeStore.scopes.getOrPut(this) {
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

