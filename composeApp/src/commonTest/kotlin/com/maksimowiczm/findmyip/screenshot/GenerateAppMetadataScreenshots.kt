package com.maksimowiczm.findmyip.screenshot

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

/** Generate screenshots for app metadata (Play Store, App Store). See justfile for usage. */
@OptIn(ExperimentalTestApi::class)
class GenerateAppMetadataScreenshots {

    @Test fun homeScreen() = runComposeUiTest { capture(HomeScreenScreenshot) }

    @Test fun settingsScreen() = runComposeUiTest { capture(SettingsScreenScreenshot) }
}

@OptIn(ExperimentalTestApi::class) expect suspend fun ComposeUiTest.capture(screenshot: Screenshot)
