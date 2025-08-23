package com.maksimowiczm.findmyip.buildlogic.plugins

import com.android.build.api.dsl.androidLibrary
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class FeatureLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(
                plugin = libs.findPlugin("androidKotlinMultiplatformLibrary").get().get().pluginId
            )
            apply(plugin = libs.findPlugin("composeMultiplatform").get().get().pluginId)
            apply(plugin = libs.findPlugin("composeCompiler").get().get().pluginId)
        }

        target.extensions.configure<KotlinMultiplatformExtension> {
            val kmp = this
            target.extensions.configure<ComposeExtension> {
                target.configureKotlinMultiplatform(kmp, this)
            }
        }
    }

    internal fun Project.configureKotlinMultiplatform(
        kmp: KotlinMultiplatformExtension,
        compose: ComposeExtension,
    ) =
        kmp.apply {
            kmp.androidLibrary {
                compileSdk = libs.findVersion("android.compileSdk").get().requiredVersion.toInt()
                minSdk = libs.findVersion("android.minSdk").get().requiredVersion.toInt()

                withHostTestBuilder {}

                withDeviceTestBuilder { sourceSetTreeName = "test" }
                    .configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
            }

            sourceSets.apply {
                commonMain.dependencies {
                    implementation(project(":shared"))
                    implementation(project(":domain"))
                    implementation(project(":application"))

                    implementation(
                        project.dependencies.platform(libs.findLibrary("koin.bom").get())
                    )
                    implementation(libs.findLibrary("koin.core").get())
                    implementation(libs.findLibrary("koin.compose").get())
                    implementation(libs.findLibrary("koin.compose.viewmodel").get())

                    implementation(libs.findLibrary("kotlinx.datetime").get())

                    implementation(compose.dependencies.runtime)
                    implementation(compose.dependencies.foundation)
                    // implementation(compose.dependencies.material3)
                    implementation(libs.findLibrary("jetbrains.compose.material3").get())
                    implementation(compose.dependencies.materialIconsExtended)
                    implementation(compose.dependencies.ui)
                    implementation(compose.dependencies.components.resources)
                    implementation(compose.dependencies.components.uiToolingPreview)
                }

                androidMain.dependencies { implementation(compose.dependencies.preview) }

                commonTest.dependencies { implementation(libs.findLibrary("kotlin.test").get()) }

                getByName("androidDeviceTest").dependencies {
                    implementation(libs.findLibrary("androidx.testRunner").get())
                    implementation(libs.findLibrary("androidx.testCore").get())
                    implementation(libs.findLibrary("androidx.testCore.ktx").get())
                    implementation(libs.findLibrary("androidx.testExt.junit").get())
                }
            }
        }
}
