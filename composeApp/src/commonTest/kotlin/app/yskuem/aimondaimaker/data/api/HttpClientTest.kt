// shared/src/commonTest/kotlin/app/yskuem/aimondaimaker/data/api/HttpClientTest.kt
package app.yskuem.aimondaimaker.data.api

import io.ktor.client.engine.mock.*
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.*
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.*

class HttpClientTest {
    @Serializable
    private data class UploadResponse(
        val success: Boolean,
        val url: String,
    )

    private val json = Json { ignoreUnknownKeys = true }

    @BeforeTest
    fun setUp() {
        val mockEngine = MockEngine { request -> makeMockResponse(request) }
        // エンジンを差し替え
        HttpClient.overrideEngine = mockEngine
    }

    @AfterTest
    fun tearDown() {
        HttpClient.overrideEngine = null // 後片付け
    }

    @Test
    fun postWithImage_sendsValidMultipart_andReturnsResponse() =
        runTest {
            // arrange
            val bytes = byteArrayOf(0x01, 0x02, 0x03)
            val response: UploadResponse =
                HttpClient.postWithImage(
                    imageBytes = bytes,
                    fileName = "avatar",
                    extension = "png",
                    path = "/v1/upload",
                )

            // assert
            assertTrue(response.success)
            assertEquals("https://example.com/image.png", response.url)
        }

    /** MockEngine が呼び出されたときに走るロジック */
    private fun MockRequestHandleScope.makeMockResponse(request: HttpRequestData): HttpResponseData {
        // ───────── リクエスト検証 ─────────
        assertEquals("/v1/upload", request.url.fullPath)
        assertEquals(HttpMethod.Post, request.method)

        // Content-Type が multipart/form-data であること
        val ctHeader = request.headers[HttpHeaders.ContentType] ?: error("No Content-Type")
        assertTrue(ctHeader.startsWith("multipart/form-data"))
        assertTrue(ctHeader.contains("boundary=")) // boundary= が含まれる

        // ───────── モックレスポンス作成 ─────────
        val bodyJson =
            json.encodeToString(
                UploadResponse(success = true, url = "https://example.com/image.png"),
            )
        return respond( // respond は MockRequestHandleScope 拡張
            content = ByteReadChannel(bodyJson),
            status = HttpStatusCode.OK,
            headers =
                headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                        .withParameter("charset", "utf-8")
                        .toString(),
                ),
        )
    }
}
