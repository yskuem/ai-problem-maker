package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable

expect class PlatformActivity

@Composable
expect fun currentPlatformActivity(): PlatformActivity?
