plugins { alias(libs.plugins.feature) }

kotlin {
    androidLibrary { namespace = "com.maksimowiczm.findmyip.feature.language" }

    val xcfName = "feature:languageKit"

    iosX64 { binaries.framework { baseName = xcfName } }

    iosArm64 { binaries.framework { baseName = xcfName } }

    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }
}
