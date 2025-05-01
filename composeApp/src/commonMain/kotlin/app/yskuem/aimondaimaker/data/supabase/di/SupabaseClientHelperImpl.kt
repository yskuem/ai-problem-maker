package app.yskuem.aimondaimaker.data.supabase.di

import app.yskuem.aimondaimaker.core.data.data.SupabaseClientHelper
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class SupabaseClientHelperImpl(
    private val supabase: SupabaseClient,
) : SupabaseClientHelper {

    internal suspend inline fun <reified T : Any> onFetchList(
        tableName: String
    ): List<T> {
        return supabase
            .from(tableName)
            .select()
            .decodeList<T>()
    }
}