package app.yskuem.aimondaimaker.core.util

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


actual typealias PlatformActivity = Activity

@Composable
actual fun currentPlatformActivity(): PlatformActivity? =
    (LocalContext.current).findActivity()