plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gradle.ktlint)
}

android {
    namespace = "com.maksimowiczm.findmyip"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.maksimowiczm.findmyip"
        minSdk = 21
        targetSdk = 35
        versionCode = 3
        versionName = "v1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "IPV4_PROVIDER", "\"https://api.ipify.org\"")
        buildConfigField("String", "IPV6_PROVIDER", "\"https://api6.ipify.org\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("demoDebug") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
            resValue("string", "app_name", "Find my IP - demo")
            signingConfig = signingConfigs.getByName("debug")
        }

        create("demo") {
            initWith(getByName("release"))
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
            resValue("string", "app_name", "Find my IP - demo")
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
        }
        getByName("debug") {
            java.srcDirs("src/release/java")
        }
        getByName("release") {
            java.srcDirs("src/release/java")
        }

        getByName("demo") {
            java.srcDirs("src/demo/java")
        }
        getByName("demoDebug") {
            java.srcDirs("src/demo/java")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }

    lint {
        disable += "MissingTranslation"
    }
}

dependencies {

    implementation(libs.androidx.material3.adaptive.navigation.suite)
    runtimeOnly(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)
    ktlintRuleset(libs.ktlint.compose)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.work)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.kotlin.result)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
