import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.google.services)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    val name = "ComposeApp"
    val xcf = XCFramework(name)
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = name
            binaryOption("bundleId", "app.yskuem.aimondaimaker")
            isStatic = true
            xcf.add(this)
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.cio)
            // CameraX core library
            implementation(libs.androidx.camera.core)
            // CameraX Camera2 extensions
            implementation(libs.androidx.camera.camera2)
            // CameraX Lifecycle library
            implementation(libs.androidx.camera.lifecycle)
            // CameraX View class
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.compose.ui.tooling.preview)

            implementation(libs.datastore.core)
            implementation(libs.datastore)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.koin.compose)
            implementation(libs.permissions.compose)
            implementation(libs.permissions.camera)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.client.logging)
            implementation(libs.supabase.auth)
            implementation(libs.supabase.postgrest)
            implementation(libs.serialization.core)
            implementation(libs.serialization.json)
            implementation(libs.multiplatform.settings.core)
            implementation(libs.multiplatform.settings.datastore)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.compose.webview.multiplatform)
            implementation(libs.basic.ads)
            implementation(libs.google.ads)
            implementation(libs.firebase.remote.config)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.firebase.remote.config)
            }
        }
    }
}

android {
    namespace = "app.yskuem.aimondaimaker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.yskuem.aimondaimaker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".stg"
        }
        create("prod") {
            dimension = "environment"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

