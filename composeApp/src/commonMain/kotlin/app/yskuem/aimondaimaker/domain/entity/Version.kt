package app.yskuem.aimondaimaker.domain.entity

data class Version(
    val value: String,
) : Comparable<Version> {
    init {
        check(value = validate(target = value))
    }

    val isEmpty: Boolean = value.isEmpty()

    private val segments: List<Int> =
        if (isEmpty) {
            emptyList()
        } else {
            value.split('.').map { it.toIntOrNull() ?: 0 }
        }

    override fun compareTo(other: Version): Int =
        segments
            .zip(other.segments)
            .fold(0) { acc, (a, b) -> if (acc != 0) acc else a.compareTo(b) }
            .let { result ->
                if (result != 0) {
                    result
                } else {
                    segments.size.compareTo(other.segments.size)
                }
            }

    companion object {
        private val VALIDATION_REGEX = "^(0|[1-9]\\d*)(\\.(?:0|[1-9]\\d*))*$".toRegex()

        fun validate(target: String): Boolean = target.isEmpty() || VALIDATION_REGEX.matches(target)
    }
}
