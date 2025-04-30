package app.yskuem.aimondaimaker.data.supabase.di

import app.yskuem.aimondaimaker.core.data.data.SupabaseClientHelper
import io.github.jan.supabase.SupabaseClient

class SupabaseClientHelperImpl(
    private val supabase: SupabaseClient,
) : SupabaseClientHelper {

}