package cafe.adriel.voyager.koin

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.ScreenModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersHolder

@Composable
inline fun <reified T : ScreenModel> koinScreenModel(noinline parameters: (() -> ParametersHolder)? = null): T {
    val koin = GlobalContext.get().koin
    return if (parameters == null) {
        koin.get()
    } else {
        koin.get(parameters = parameters)
    }
}
