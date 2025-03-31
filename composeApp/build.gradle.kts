import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.gmazzo.buildconfig)
}

buildConfig {
    packageName("com.maksimowiczm.findmyip")
    className("BuildConfig")

    buildConfigField("String", "IPV4_PROVIDER", "\"${properties["ipify.api.url"]}\"")
    buildConfigField("String", "IPV6_PROVIDER", "\"${properties["ipify.api6.url"]}\"")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)

            implementation(libs.bundles.koin.android)

            implementation(libs.androidx.work.runtime.ktx)

            implementation(libs.ktor.client.okhttp)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.androidx.sqlite.bundle)
            implementation(libs.ktor.client.java)
        }
        commonMain.dependencies {
            implementation(libs.kermit)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material3AdaptiveNavigationSuite)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.navigation.compose)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.paging)

            implementation(libs.androidx.datastore.preferences)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            implementation(libs.compose.shimmer)

            implementation(libs.bundles.flowmvi)

            implementation(libs.ktor.client.core)
        }
    }
}

android {
    namespace = "com.maksimowiczm.findmyip"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.maksimowiczm.findmyip"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 9
        versionName = "v2.0.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(libs.flowmvi.debugger)
    listOf("kspAndroid", "kspDesktop").forEach {
        add(it, libs.androidx.room.compiler)
    }
}

// Experimental and forgotten
compose.desktop {
    application {
        mainClass = "com.maksimowiczm.findmyip.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.maksimowiczm.findmyip"
            packageVersion = "1.0.0"
        }
    }
}

composeCompiler {
    stabilityConfigurationFiles.add(
        rootProject.layout.projectDirectory.file("stability_definitions.txt")
    )
}
