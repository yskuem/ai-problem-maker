package app.yskuem.aimondaimaker.data.supabase.config

import io.github.jan.supabase.SupabaseClientBuilder

internal actual fun SupabaseClientBuilder.configurePlatformHttpEngine() {
    // Darwin engine is provided by default through Ktor on iOS.
}
