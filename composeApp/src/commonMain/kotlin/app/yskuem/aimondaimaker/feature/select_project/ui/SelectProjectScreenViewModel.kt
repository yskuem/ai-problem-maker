package app.yskuem.aimondaimaker.feature.select_project.ui

import app.yskuem.aimondaimaker.core.data.data.SupabaseClientHelper
import app.yskuem.aimondaimaker.data.api.response.ProjectResponse
import app.yskuem.aimondaimaker.data.supabase.SupabaseColumnName
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch

class SelectProjectScreenViewModel(
    private val supabaseClientHelper: SupabaseClientHelper
) : ScreenModel {
    init {
        onFetchProjectList()
    }

    fun onFetchProjectList() {
        screenModelScope.launch {
            val result = runCatching {
                supabaseClientHelper.fetchListByMatchValue(
                    tableName = "test",
                    filterCol = SupabaseColumnName.Project.ID,
                    filterVal = "7828121",
                    orderCol = SupabaseColumnName.Project.CREATED_AT,
                    serializer = ProjectResponse.serializer(),
                )
            }
            result.onSuccess {
                val kk = it
                println("Fetched project list: ${it[0].id}")
            }.onFailure {
                println("Failed to fetch project list: ${it.message}")
            }
        }
    }
}