plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    sourceSets.all { languageSettings.enableLanguageFeature("ExpectActualClasses") }

    androidLibrary {
        namespace = "com.maksimowiczm.findmyip.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {}

        withDeviceTestBuilder { sourceSetTreeName = "test" }
            .configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    val xcfName = "sharedKit"

    iosX64 { binaries.framework { baseName = xcfName } }

    iosArm64 { binaries.framework { baseName = xcfName } }

    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

    sourceSets {
        androidMain.dependencies { implementation(libs.androidx.appcompat) }

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

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)

            implementation(libs.kotlinx.datetime)

            implementation(libs.androidx.paging.common)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "findmyip.composeapp.generated.resources"
    generateResClass = always
}
