plugins { `kotlin-dsl` }

group = "com.maksimowiczm.findmyip.build_logic"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    compileOnly(libs.gradle.android)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.compose)
}

gradlePlugin {
    plugins {
        register("library-feature") {
            id = "com.maksimowiczm.findmyip.plugins.libraries.feature"
            implementationClass =
                "com.maksimowiczm.findmyip.buildlogic.plugins.FeatureLibraryConventionPlugin"
        }
    }
}
