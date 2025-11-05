package app.yskuem.aimondaimaker

interface Platform {
    val name: String
    val isIPad: Boolean
}

expect fun getPlatform(): Platform
