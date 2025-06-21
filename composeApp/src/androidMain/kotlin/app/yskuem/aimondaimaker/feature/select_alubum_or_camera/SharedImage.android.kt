package app.yskuem.aimondaimaker.feature.select_alubum_or_camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual class SharedImage(
    private val bitmap: android.graphics.Bitmap?,
) {
    actual fun toByteArray(): ByteArray? =
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                COMPRESSION_QUALITY,
                byteArrayOutputStream,
            )
            byteArrayOutputStream.toByteArray()
        } else {
            println("toByteArray null")
            null
        }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
        } else {
            println("toImageBitmap null")
            null
        }
    }

    private companion object {
        const val COMPRESSION_QUALITY = 90
    }
}
