package app.yskuem.aimondaimaker.data.api

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual fun createHttpClientEngine(): HttpClientEngine = OkHttp.create()
