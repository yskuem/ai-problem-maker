# Keep Android components referenced from the manifest.
-keep class app.yskuem.aimondaimaker.MyApp { *; }
-keep class app.yskuem.aimondaimaker.MainActivity { *; }
-keep class app.yskuem.aimondaimaker.core.picker.ComposeFileProvider { *; }

# Ensure kotlinx.serialization generated serializers are retained.
-keep class **$$serializer { *; }
-keepclassmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep @kotlinx.serialization.Serializable class *

# Ignore warnings from Ktor optional dependencies.
-dontwarn io.ktor.**
