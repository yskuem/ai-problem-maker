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

    private companion object {
        val TEST_IMAGE_BYTES = byteArrayOf(0x01, 0x02, 0x03)
        const val TEST_FILE_NAME = "avatar"
        const val TEST_FILE_EXTENSION = "png"
        const val TEST_UPLOAD_PATH = "/v1/upload"
        const val TEST_RESPONSE_URL = "https://example.com/image.png"
    }

    private val json = Json { ignoreUnknownKeys = true }

    @BeforeTest
    fun setUp() {
        val mockEngine = MockEngine { request -> handleMockRequest(request) }
        HttpClient.overrideEngine = mockEngine
    }

    @AfterTest
    fun tearDown() {
        HttpClient.overrideEngine = null
    }

    @Test
    fun postWithImage_sendsValidMultipart_andReturnsResponse() = runTest {
        // Act
        val response: UploadResponse = HttpClient.postWithImage(
            imageBytes = TEST_IMAGE_BYTES,
            fileName = TEST_FILE_NAME,
            extension = TEST_FILE_EXTENSION,
            path = TEST_UPLOAD_PATH,
        )

        // Assert
        assertTrue(response.success)
        assertEquals(TEST_RESPONSE_URL, response.url)
    }

    private fun MockRequestHandleScope.handleMockRequest(request: HttpRequestData): HttpResponseData {
        validateRequest(request)
        return createMockResponse()
    }

    private fun validateRequest(request: HttpRequestData) {
        assertEquals(TEST_UPLOAD_PATH, request.url.fullPath)
        assertEquals(HttpMethod.Post, request.method)
        validateMultipartContentType(request)
    }

    private fun validateMultipartContentType(request: HttpRequestData) {
        val contentTypeHeader = request.headers[HttpHeaders.ContentType] 
            ?: error("Content-Type header is missing")
        
        assertTrue(
            contentTypeHeader.startsWith("multipart/form-data"),
            "Expected multipart/form-data, but got: $contentTypeHeader"
        )
        assertTrue(
            contentTypeHeader.contains("boundary="),
            "Boundary parameter is missing in Content-Type header"
        )
    }

    private fun MockRequestHandleScope.createMockResponse(): HttpResponseData {
        val responseBody = json.encodeToString(
            UploadResponse(success = true, url = TEST_RESPONSE_URL)
        )
        
        return respond(
            content = ByteReadChannel(responseBody),
            status = HttpStatusCode.OK,
            headers = headersOf(
                HttpHeaders.ContentType,
                ContentType.Application.Json.withParameter("charset", "utf-8").toString()
            ),
        )
    }
}
