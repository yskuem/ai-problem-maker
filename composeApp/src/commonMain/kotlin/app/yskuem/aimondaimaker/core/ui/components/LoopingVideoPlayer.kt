package app.yskuem.aimondaimaker.core.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import chaintech.videoplayer.host.MediaPlayerEvent
import chaintech.videoplayer.host.MediaPlayerHost
import chaintech.videoplayer.model.VideoPlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerComposable

@Composable
fun LoopingVideoPlayer(
    url: String,
    modifier: Modifier = Modifier,
    isMuted: Boolean = false,
) {
    val playerHost =
        remember {
            MediaPlayerHost(
                mediaUrl = url,
                isPaused = false,
                isMuted = isMuted,
                isLooping = true,
            )
        }

    playerHost.onEvent = { event ->
        if (event == MediaPlayerEvent.MediaEnd) {
            playerHost.seekTo(0f)
            playerHost.play()
        }
    }

    VideoPlayerComposable(
        modifier = modifier.fillMaxSize(),
        playerHost = playerHost,
        playerConfig =
            VideoPlayerConfig(
                showControls = false,
                isSeekBarVisible = false,
                isDurationVisible = false,
                isSpeedControlEnabled = false,
                isFullScreenEnabled = false,
                isMuteControlEnabled = false,
                isFastForwardBackwardEnabled = false,
            ),
    )

    DisposableEffect(playerHost) {
        onDispose {
            runCatching { playerHost.pause() }
        }
    }
}
