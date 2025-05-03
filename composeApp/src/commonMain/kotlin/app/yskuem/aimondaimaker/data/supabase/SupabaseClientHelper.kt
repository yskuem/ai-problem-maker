package app.yskuem.aimondaimaker.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.Json

class SupabaseClientHelper(
    private val supabase: SupabaseClient,
    private val json: Json
) {
    internal suspend inline fun<reified T : Any> addItem(
        tableName: String,
        item: T,
    ): String {
        return supabase.from(tableName)
            .insert<T>(item)
            .data
    }

    internal suspend inline fun <reified T : Any> fetchListByMatchValue(
        tableName: String,
        filterCol: String,
        filterVal: String,
        orderCol: String
    ): List<T> {
        return supabase
            .from(tableName)
            .select {
                filter { eq(filterCol, filterVal) }
                order(column = orderCol, order = Order.DESCENDING)
            }
            .decodeList<T>()
    }
}