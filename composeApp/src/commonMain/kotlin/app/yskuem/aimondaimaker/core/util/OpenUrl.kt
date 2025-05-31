package app.yskuem.aimondaimaker.core.util

import androidx.compose.runtime.Composable


interface OpenUrl {
    fun handle(url: String): Unit
}