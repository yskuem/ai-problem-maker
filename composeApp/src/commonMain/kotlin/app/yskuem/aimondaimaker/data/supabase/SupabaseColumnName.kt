package app.yskuem.aimondaimaker.data.supabase

object SupabaseColumnName {
    const val CREATED_AT = "created_at"
    const val UPDATED_AT = "updated_at"
    const val PROJECT_ID = "project_id"

    object Project {
        const val ID = "id"
        const val CREATE_USER_ID = "created_user_id"
    }

    object User {
        const val AVATAR_URL = "avatar_url"
    }

    object Quiz {
        const val CREATED_USER_ID = "created_user_id"
        const val GROUP_ID = "group_id"
    }
}
