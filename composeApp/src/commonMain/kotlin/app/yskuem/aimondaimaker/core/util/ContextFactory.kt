package app.yskuem.aimondaimaker.core.util

expect class ContextFactory() {
    fun getContext(): Any
    fun getApplication(): Any
    fun getActivity(): Any
}