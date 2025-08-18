plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.gmazzo.buildconfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

buildConfig {
    packageName("com.maksimowiczm.findmyip.infrastructure")
    className("BuildConfig")

    val useFake = true
    buildConfigField("Boolean", "USE_FAKE", "$useFake")
}

room { schemaDirectory("$projectDir/schemas") }

kotlin {
    androidLibrary {
        namespace = "com.maksimowiczm.findmyip.infrastructure"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withHostTestBuilder {}

        withDeviceTestBuilder { sourceSetTreeName = "test" }
            .configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
    }

    val xcfName = "infrastructureKit"

    iosX64 { binaries.framework { baseName = xcfName } }

    iosArm64 { binaries.framework { baseName = xcfName } }

    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.application)
            implementation(projects.domain)
            implementation(libs.ktor.client.core)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.room.paging)
        }

        commonTest.dependencies { implementation(libs.androidx.room.testing) }

        androidMain.dependencies { implementation(libs.ktor.client.okhttp) }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.testRunner)
            implementation(libs.androidx.core)
            implementation(libs.androidx.testExt.junit)
        }

        iosMain.dependencies { implementation(libs.ktor.client.darwin) }
    }
}

dependencies {
    listOf(
            "kspCommonMainMetadata",
            "kspAndroid",
            "kspIosX64",
            "kspIosArm64",
            "kspIosSimulatorArm64",
        )
        .forEach { add(it, libs.androidx.room.compiler) }
}
