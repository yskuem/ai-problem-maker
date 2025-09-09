package app.yskuem.aimondaimaker.core.util

import android.app.Application
import android.content.Context
import androidx.activity.ComponentActivity
import java.lang.ref.WeakReference

object ActivityProvider {
    private var activityRef: WeakReference<ComponentActivity>? = null

    fun setActivity(activity: ComponentActivity) {
        activityRef = WeakReference(activity)
    }

    fun getActivity(): ComponentActivity? = activityRef?.get()

    fun getContext(): Context? = activityRef?.get()?.baseContext

    fun getApplication(): Application? = activityRef?.get()?.application
}

