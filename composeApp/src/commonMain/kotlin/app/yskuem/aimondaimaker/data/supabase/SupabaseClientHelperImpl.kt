package app.yskuem.aimondaimaker.data.supabase

import app.yskuem.aimondaimaker.core.data.data.SupabaseClientHelper
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class SupabaseClientHelperImpl(
    private val supabase: SupabaseClient,
    private val json: Json
) : SupabaseClientHelper {
    override suspend fun <T> fetchListByMatchValue(
        tableName: String,
        serializer: KSerializer<T>,
        filterCol: String,
        filterVal: String,
        orderCol: String
    ): List<T> {
        val response = supabase
            .from(tableName)
            .select {
                filter { eq(filterCol, filterVal) }
                order(column = orderCol, order = Order.DESCENDING)
            }
            .data
        return json.decodeFromString(
            ListSerializer(serializer),
            response
        )
    }
}