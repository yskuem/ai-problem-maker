package app.yskuem.aimondaimaker.data.supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.Json

class SupabaseClientHelper(
    private val supabase: SupabaseClient,
    private val json: Json,
) {
    /**
     * テーブルに新しいアイテムを挿入します。
     *
     * @param tableName 挿入先のテーブル名
     * @param item 挿入するデータクラスのインスタンス
     * @return 実行結果のデータ文字列
     */
    internal suspend inline fun <reified T : Any> addItem(
        tableName: String,
        item: T,
    ): String =
        supabase
            .from(tableName)
            .insert<T>(item)
            .data

    /**
     * 指定したカラム(filterCol)が filterVal と一致するレコードを取得し、
     * 指定したカラム(orderCol)で降順にソートして返します。
     *
     * @param tableName 検索対象のテーブル名
     * @param filterCol フィルタに使用するカラム名
     * @param filterVal フィルタに使用する値
     * @param orderCol ソートに使用するカラム名（降順）
     * @return 条件に合致するレコードをデコードした T 型のリスト
     */
    internal suspend inline fun <reified T : Any> fetchListByMatchValue(
        tableName: String,
        filterCol: String,
        filterVal: String,
        orderCol: String,
    ): List<T> =
        supabase
            .from(tableName)
            .select {
                filter { eq(filterCol, filterVal) }
                order(column = orderCol, order = Order.DESCENDING)
            }.decodeList<T>()

    /**
     * 指定したカラム(filterCol)が filterVal と一致するレコードを
     * changes で渡したデータクラス T のプロパティで更新し、更新後のレコード群を返します。
     *
     * @param tableName 更新対象テーブル名
     * @param filterCol フィルタに使うカラム名
     * @param filterVal フィルタに使う値
     * @param changes 更新内容を持つデータクラスのインスタンス
     * @return 更新後のレコードを T 型のリストで返却
     */
    internal suspend inline fun <reified T : Any> updateItemByMatch(
        tableName: String,
        filterCol: String,
        filterVal: String,
        changes: T,
    ): List<T> =
        supabase
            .from(tableName)
            .update<T>(changes) {
                filter { eq(filterCol, filterVal) }
            }.decodeList<T>()

    /**
     * 主キー(idCol)で一意にレコードを指定して、
     * changes で渡したデータクラス T のプロパティで更新し、更新後のレコードを返します。
     *
     * @param tableName 更新対象テーブル名
     * @param idCol 主キーのカラム名 (例: "id")
     * @param idVal 主キーの値
     * @param changes 更新内容を持つデータクラスのインスタンス
     * @return 更新後のレコード (該当レコードがなければ null)
     */
    internal suspend inline fun <reified T : Any> updateItemById(
        tableName: String,
        idCol: String,
        idVal: String,
        changes: T,
    ): T? =
        supabase
            .from(tableName)
            .update<T>(changes) {
                select()
                filter { eq(idCol, idVal) }
            }.decodeSingleOrNull<T>()
}
