plugins { alias(libs.plugins.feature) }

kotlin {
    androidLibrary { namespace = "com.maksimowiczm.findmyip.feature.home" }

    val xcfName = "feature:homeKit"

    iosX64 { binaries.framework { baseName = xcfName } }

    iosArm64 { binaries.framework { baseName = xcfName } }

    iosSimulatorArm64 { binaries.framework { baseName = xcfName } }

    sourceSets.commonMain.dependencies { implementation(libs.androidx.paging.common) }
}
