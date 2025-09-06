package app.yskuem.aimondaimaker.data.api

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.getFlavor
import app.yskuem.aimondaimaker.data.api.config.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object HttpClient {
    private val HOST =
        when (getFlavor()) {
            Flavor.DEV, Flavor.STAGING -> ApiConfig.DEV_HOST
            Flavor.PROD -> ApiConfig.PROD_HOST
        }
    private val engine = createHttpClientEngine()
    private val jsonConfig =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
        }

    val client: HttpClient by lazy {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(jsonConfig)
            }
            install(Logging) {
                level = LogLevel.INFO
            }
            install(HttpTimeout) {
                // リクエスト全体の許容時間（ミリ秒）
                requestTimeoutMillis = 150_000
                // TCP接続確立のタイムアウト
                connectTimeoutMillis = 150_000
                // データ受信間隔の最大タイムアウト
                socketTimeoutMillis = 150_000
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = HOST
                }
            }
        }
    }

    suspend inline fun <reified T> postWithImage(
        imageBytes: ByteArray,
        fileName: String,
        extension: String,
        path: String,
    ): T {
        // Determine ContentType based on extension
        val contentType =
            when (extension.lowercase()) {
                "png" -> ContentType.Image.PNG
                "jpg", "jpeg" -> ContentType.Image.JPEG
                "gif" -> ContentType.Image.GIF
                "svg" -> ContentType.Image.SVG
                "bmp" -> ContentType.parse("image/bmp")
                else -> ContentType.Application.OctetStream
            }

        val response =
            client.post(path) {
                // Multipart/form-data body
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = imageBytes,
                                headers =
                                    Headers.build {
                                        // Name and filename set dynamically
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "form-data; name=\"file\"; filename=\"$fileName.$extension\"",
                                        )
                                        append(HttpHeaders.ContentType, contentType.toString())
                                    },
                            )
                        },
                    ),
                )
            }

        val responseText = response.bodyAsText()

        return try {
            jsonConfig.decodeFromString<T>(responseText)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to decode response: $responseText", e)
        }
    }
}
