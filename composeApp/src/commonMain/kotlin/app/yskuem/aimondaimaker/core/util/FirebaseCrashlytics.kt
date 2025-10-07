package app.yskuem.aimondaimaker.core.util

interface FirebaseCrashlytics {
    fun log(message: String)
}

class FirebaseFirebaseCrashlyticsImpl(
    private val firebaseCrashlytics: dev.gitlive.firebase.crashlytics.FirebaseCrashlytics,
) : FirebaseCrashlytics {
    override fun log(message: String) {
        firebaseCrashlytics.log(message = message)
    }
}
