package app.yskuem.aimondaimaker.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.*

object HttpClient {
    private const val HOST = "ai-problem-maker-mu.vercel.app"
    private val engine = createHttpClientEngine()

    val client: HttpClient by lazy {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    coerceInputValues = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
            install(HttpTimeout) {
                // リクエスト全体の許容時間（ミリ秒）
                requestTimeoutMillis = 60_000
                // TCP接続確立のタイムアウト
                connectTimeoutMillis = 60_000
                // データ受信間隔の最大タイムアウト
                socketTimeoutMillis = 60_000
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host     = HOST
                }
            }
        }
    }

    suspend inline fun <reified T> postWithImage(
        imageBytes: ByteArray,
        fileName: String,
        extension: String
    ): T {
        // Determine ContentType based on extension
        val contentType = when (extension.lowercase()) {
            "png" -> ContentType.Image.PNG
            "jpg", "jpeg" -> ContentType.Image.JPEG
            "gif" -> ContentType.Image.GIF
            "svg" -> ContentType.Image.SVG
            "bmp" -> ContentType.parse("image/bmp")
            else -> ContentType.Application.OctetStream
        }

        return client.post("/analyze_image") {
            // Multipart/form-data body
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append(
                            key = "file",
                            value = imageBytes,
                            headers = Headers.build {
                                // Name and filename set dynamically
                                append(HttpHeaders.ContentDisposition, "form-data; name=\"file\"; filename=\"$fileName.$extension\"")
                                append(HttpHeaders.ContentType, contentType.toString())
                            }
                        )
                    }
                )
            )
        }.body()
    }
}

