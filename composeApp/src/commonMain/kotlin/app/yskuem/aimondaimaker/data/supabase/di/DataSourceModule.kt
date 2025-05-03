package app.yskuem.aimondaimaker.data.supabase.di

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://boalkdckbrgxekncokjz.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJvYWxrZGNrYnJneGVrbmNva2p6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDU5MDI0ODIsImV4cCI6MjA2MTQ3ODQ4Mn0.SJGwz6WU9QZOCOhLb2HU9DYVdVf_2jo8NCkiCAdlwqw",
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }
    single {
        SupabaseClientHelper(
            supabase = get(),
            json = get()
        )
    }
}