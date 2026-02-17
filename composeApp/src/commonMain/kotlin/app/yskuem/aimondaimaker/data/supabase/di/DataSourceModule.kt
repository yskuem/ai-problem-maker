package app.yskuem.aimondaimaker.data.supabase.di

import app.yskuem.aimondaimaker.data.supabase.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.supabase.handler.SupabaseConfigHelper
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.appleNativeLogin
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val supabaseModule =
    module {
        single {
            createSupabaseClient(
                supabaseUrl = SupabaseConfigHelper.getSupabaseUrl(),
                supabaseKey = SupabaseConfigHelper.getSupabaseAnonKey(),
            ) {
                install(Auth)
                install(Postgrest)
                install(ComposeAuth) {
                    googleNativeLogin(serverClientId = "YOUR_GOOGLE_SERVER_CLIENT_ID")
                    appleNativeLogin()
                }
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
                json = get(),
            )
        }
    }

