package app.yskuem.aimondaimaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.yskuem.aimondaimaker.core.di.initKoin
import com.google.firebase.FirebaseApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        FileKit.init(this)
        initKoin()
        FirebaseApp.initializeApp(this)

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
