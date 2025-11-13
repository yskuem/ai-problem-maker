package app.yskuem.aimondaimaker

import ai_problem_maker.composeapp.generated.resources.Res
import ai_problem_maker.composeapp.generated.resources.ic_splash_back
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import app.yskuem.aimondaimaker.core.ui.components.FullScreenPhotoBackground
import app.yskuem.aimondaimaker.core.ui.components.SplashLottie
import app.yskuem.aimondaimaker.core.ui.components.SplashScreen
import com.google.firebase.FirebaseApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import org.jetbrains.compose.resources.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        FirebaseApp.initializeApp(this)

        FileKit.init(this)

        setContent {
            App()
        }
    }

    companion object {
        lateinit var instance: MainActivity
            private set
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
