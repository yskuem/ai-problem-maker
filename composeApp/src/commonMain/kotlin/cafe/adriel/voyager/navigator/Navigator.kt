package cafe.adriel.voyager.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cafe.adriel.voyager.core.screen.Screen
import java.util.UUID

val LocalNavigator = compositionLocalOf<Navigator?> { null }

class Navigator(private val navController: NavHostController, private val screenMap: MutableMap<String, Screen>) {
    fun push(screen: Screen) {
        val id = UUID.randomUUID().toString()
        screenMap[id] = screen
        navController.navigate("screen/$id")
    }
    fun replace(screen: Screen) {
        navController.popBackStack()
        push(screen)
    }
    fun pop() {
        navController.popBackStack()
    }
}

@Composable
fun Navigator(startScreen: Screen) {
    val navController = rememberNavController()
    val screenMap = remember { mutableStateMapOf<String, Screen>() }
    val navigator = remember(navController) { Navigator(navController, screenMap) }
    var startId by remember { mutableStateOf("") }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        LaunchedEffect(startScreen) {
            val id = UUID.randomUUID().toString()
            screenMap[id] = startScreen
            startId = id
            navController.navigate("screen/$id") {
                popUpTo(0)
            }
        }
        if (startId.isNotEmpty()) {
            NavHost(navController = navController, startDestination = "screen/$startId") {
                composable("screen/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id") ?: return@composable
                    screenMap[id]?.Content()
                }
            }
        }
    }
}
