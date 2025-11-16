import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.plugin)
    alias(libs.plugins.mokkery)
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
            implementation(libs.play.review)
            implementation(libs.google.ads)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(libs.ui.backhandler)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.voyager.koin)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.permissions.compose)
            implementation(libs.permissions.camera)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.filekit.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
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
            implementation(libs.firebase.remote.config)
            implementation(libs.firebase.analytics)
            implementation(libs.firebase.crashlytics)
            implementation(libs.kotlinx.datetime)
            implementation(libs.compose.multiplatform.media.player)
            implementation(libs.kmp.onboarding)
            implementation(libs.compottie)
            implementation(libs.compottie.resources)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation (libs.kotlin.test)
            implementation (libs.kotlin.coroutines.test)
            implementation (libs.kotlin.test.common)
            implementation (libs.kotlin.test.annotations.common)
            implementation(libs.mokkery.core)
            implementation(libs.turbine)
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.firebase.remote.config)
            }
        }
    }
}

val keystorePropertiesFile = rootProject.file("key.properties")


android {
    namespace = "app.yskuem.aimondaimaker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.yskuem.aimondaimaker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 18
        versionName = "1.1.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        if(keystorePropertiesFile.exists()) {
            val keystoreProperties = Properties().apply {
                load(keystorePropertiesFile.inputStream())
            }
            create("release") {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        } else {
            create("release") {}
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
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

