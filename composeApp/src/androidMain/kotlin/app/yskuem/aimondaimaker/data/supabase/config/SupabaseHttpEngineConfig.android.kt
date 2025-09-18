package app.yskuem.aimondaimaker.data.supabase.config

import io.github.jan.supabase.SupabaseClientBuilder
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Dns
import java.util.concurrent.TimeUnit

internal actual fun SupabaseClientBuilder.configurePlatformHttpEngine() {
    httpEngine = {
        OkHttp.create {
            config {
                dns(Dns.SYSTEM)
                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }
        }
    }
}
