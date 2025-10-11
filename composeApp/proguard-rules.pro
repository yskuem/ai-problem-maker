# Keep Koin definitions that rely on reflection.
-keep class org.koin.** { *; }
-keep class * extends org.koin.core.component.KoinComponent { *; }

# Keep Kotlin serialization metadata generated for Serializable classes.
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# Keep Supabase and Ktor generated serializers used via reflection.
-keep class io.ktor.** { *; }
-keep class io.supabase.** { *; }
