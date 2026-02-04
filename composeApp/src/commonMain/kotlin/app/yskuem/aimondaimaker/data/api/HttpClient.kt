package app.yskuem.aimondaimaker.data.api

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.getFlavor
import app.yskuem.aimondaimaker.data.api.config.ApiConfig
import app.yskuem.aimondaimaker.data.api.response.PdfResponse
import app.yskuem.aimondaimaker.feature.quiz.config.GENERATE_PDF_BASE_UEL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsBytes
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.toByteArray
import kotlinx.serialization.json.Json

object HttpClient {
    private val HOST =
        when (getFlavor()) {
            Flavor.DEV, Flavor.STAGING -> ApiConfig.DEV_HOST
            Flavor.PROD -> ApiConfig.PROD_HOST
        }
    private val engine = createHttpClientEngine()

    val client: HttpClient by lazy {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        coerceInputValues = true
                    },
                )
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

        return client
            .post(path) {
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
            }.body()
    }

    suspend inline fun <reified REQ> postForGeneratePdf(
        requestModel: REQ,
        path: String = "/generate-pdf",
        baseUrl: String = GENERATE_PDF_BASE_UEL,
    ): PdfResponse {
        val res: HttpResponse =
            client.post("$baseUrl$path") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Pdf)
                setBody(requestModel)
            }

        // 1) HTTP ステータス検証
        if (!res.status.isSuccess()) {
            val msg = runCatching { res.bodyAsText() }.getOrNull()
            throw IllegalStateException("HTTP ${res.status.value}: ${msg ?: "no message"}")
        }

        // 2) Content-Type 検証
        val ct = res.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
        val isPdf =
            ct?.match(ContentType.Application.Pdf) == true ||
                (ct?.contentType == "application" && ct.contentSubtype == "pdf") ||
                (ct?.contentType == "application" && ct.contentSubtype == "octet-stream")

        val bytes = res.bodyAsBytes()

        // 3) PDF ヘッダ検証（%PDF-）
        if (!isPdf || bytes.size < 5 ||
            bytes[0] != 0x25.toByte() || // %
            bytes[1] != 0x50.toByte() || // P
            bytes[2] != 0x44.toByte() || // D
            bytes[3] != 0x46.toByte() || // F
            bytes[4] != 0x2D.toByte() // -
        ) {
            val preview = bytes.hexHead(16)
            throw IllegalStateException("Response is not PDF. Content-Type=$ct, head=$preview")
        }

        return PdfResponse(
            filename = extractFilename(res.headers[HttpHeaders.ContentDisposition]),
            bytes = bytes,
        )
    }

    fun extractFilename(contentDisposition: String?): String? {
        if (contentDisposition.isNullOrBlank()) return null
        val regex = Regex("""filename\*?=(?:UTF-8''|")?([^\";]+)""")
        return regex.find(contentDisposition)?.groupValues?.getOrNull(1)
    }

    fun ByteArray.hexHead(n: Int): String =
        this.take(n).joinToString(" ") { b ->
            ((b.toInt() and 0xFF).toString(16)).uppercase().padStart(2, '0')
        }
}
