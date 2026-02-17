package app.yskuem.aimondaimaker.data.supabase.di

import app.yskuem.aimondaimaker.core.config.DEV_GOOGLE_SERVER_CLIENT_ID
import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.PROD_GOOGLE_SERVER_CLIENT_ID
import app.yskuem.aimondaimaker.core.config.STAGING_GOOGLE_SERVER_CLIENT_ID
import app.yskuem.aimondaimaker.core.config.getFlavor
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
                install(Auth) {
                    scheme = "app.yskuem.aimondaimaker"
                    host = "callback"
                }
                install(Postgrest)
                install(ComposeAuth) {
                    // TODO prodとdevで分ける
                    googleNativeLogin(serverClientId = getServerClientId())
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


private fun getServerClientId(): String {
    return when(getFlavor()) {
        Flavor.DEV -> DEV_GOOGLE_SERVER_CLIENT_ID
        Flavor.STAGING -> STAGING_GOOGLE_SERVER_CLIENT_ID
        Flavor.PROD -> PROD_GOOGLE_SERVER_CLIENT_ID
    }
}
