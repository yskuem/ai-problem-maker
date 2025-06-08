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
            supabaseUrl = "https://yexqdytmyenkzpornjhx.supabase.co",//"https://ozpxhlodwuwifuzbnryy.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InlleHFkeXRteWVua3pwb3Juamh4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYyNzY0NDgsImV4cCI6MjA2MTg1MjQ0OH0.MXdfk2WqDCOMmsw9zerJ4KpCicm2Yyl8LhqirKCXZzU"//"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im96cHhobG9kd3V3aWZ1emJucnl5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDg3NDY5NTgsImV4cCI6MjA2NDMyMjk1OH0.M05unoLVOCoAz0a4aV-gwUCHaEKPQNJfQYFNUg7oGaQ",
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