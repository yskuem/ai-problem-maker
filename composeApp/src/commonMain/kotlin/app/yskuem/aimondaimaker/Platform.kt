package app.yskuem.aimondaimaker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
