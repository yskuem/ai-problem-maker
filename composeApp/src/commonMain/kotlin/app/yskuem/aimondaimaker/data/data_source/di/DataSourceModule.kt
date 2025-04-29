package app.yskuem.aimondaimaker.data.data_source.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.dsl.module

val dataSourceModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://your-supabase-url.supabase.co",
            supabaseKey = "your-supabase-key",
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}