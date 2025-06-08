package app.yskuem.aimondaimaker.data.supabase.handler

import app.yskuem.aimondaimaker.core.config.Flavor
import app.yskuem.aimondaimaker.core.config.getFlavor
import app.yskuem.aimondaimaker.data.supabase.config.SupabaseDevSettings
import app.yskuem.aimondaimaker.data.supabase.config.SupabaseProdSettings

object SupabaseConfigHelper {
    fun getSupabaseUrl(): String = when(getFlavor()) {
        Flavor.DEV -> SupabaseDevSettings.URL
        Flavor.STAGING -> SupabaseDevSettings.URL
        Flavor.PROD -> SupabaseProdSettings.URL
    }

    fun getSupabaseAnonKey(): String = when(getFlavor()) {
        Flavor.DEV -> SupabaseDevSettings.ANON
        Flavor.STAGING -> SupabaseDevSettings.ANON
        Flavor.PROD -> SupabaseProdSettings.ANON
    }
}
