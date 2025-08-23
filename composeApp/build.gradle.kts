@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.gmazzo.buildconfig)
}

buildConfig {
    val feedbackEmail = "maksimowicz.dev@gmail.com"
    buildConfigField("String", "FEEDBACK_EMAIL", "\"$feedbackEmail\"")

    val feedbackEmailUri = "mailto:$feedbackEmail?subject=Find my IP Feedback"
    buildConfigField("String", "FEEDBACK_EMAIL_URI", "\"$feedbackEmailUri\"")

    val githubUrl = "https://github.com/maksimowiczm/find-my-ip"
    val githubIssues = "$githubUrl/issues"
    buildConfigField("String", "GITHUB_URL", "\"$githubUrl\"")
    buildConfigField("String", "GITHUB_ISSUES_URL", "\"$githubIssues\"")

    val crowdin = "https://crowdin.com/project/find-my-ip"
    buildConfigField("String", "CROWDIN_URL", "\"$crowdin\"")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)

        dependencies {
            debugImplementation(libs.androidx.compose.ui.test.manifest)
            androidTestImplementation(libs.androidx.compose.ui.test.junit4)
        }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            // implementation(compose.material3)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.navigation)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlinx.datetime)

            implementation(libs.androidx.paging.common)

            implementation(projects.shared)
            implementation(projects.domain)
            implementation(projects.infrastructure)
            implementation(projects.application)
            implementation(projects.feature.home)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(ExperimentalComposeLibrary::class) implementation(compose.uiTest)
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
        versionCode = libs.versions.android.versionCode.get().toInt()
        versionName = libs.versions.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes {
        getByName("release") { isMinifyEnabled = true }
        create("devRelease") {
            initWith(getByName("release"))
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        create("miniDevRelease") {
            initWith(getByName("devRelease"))
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies { debugImplementation(compose.uiTooling) }
