package app.yskuem.aimondaimaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowInsetsControllerCompat
import app.yskuem.aimondaimaker.core.di.initKoin
import app.yskuem.aimondaimaker.core.util.ActivityProvider
import com.google.firebase.FirebaseApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityProvider.setActivity(this)

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        FileKit.init(this)
        initKoin()
        FirebaseApp.initializeApp(this)

        setContent {
            App()
        }
    }

}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
