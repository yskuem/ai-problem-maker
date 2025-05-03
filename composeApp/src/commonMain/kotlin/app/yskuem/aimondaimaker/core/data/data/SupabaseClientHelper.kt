package app.yskuem.aimondaimaker.core.data.data

import kotlinx.serialization.KSerializer

interface SupabaseClientHelper {
    suspend fun <T> fetchListByMatchValue(
        tableName: String,
        serializer: KSerializer<T>,
        filterCol: String,
        filterVal: String,
        orderCol: String
    ): List<T>
}