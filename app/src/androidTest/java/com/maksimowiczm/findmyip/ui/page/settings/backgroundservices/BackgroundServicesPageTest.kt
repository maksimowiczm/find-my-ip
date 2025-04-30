package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageTestTags.CONTENT
import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SETTING
import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageTestTags.GO_BACK_BUTTON
import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageTestTags.PERIODIC_WORK_SETTING
import com.maksimowiczm.findmyip.ui.page.settings.backgroundservices.BackgroundServicesPageTestTags.TIPS
import org.junit.Rule
import org.junit.Test

class BackgroundServicesPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun backgroundServicesPage(): Unit = composeTestRule.run {
        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(GO_BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(CONTENT).assertIsDisplayed()
        onNodeWithTag(CONTENT).performScrollToNode(hasTestTag(FOREGROUND_SERVICE_SETTING))
        onNodeWithTag(FOREGROUND_SERVICE_SETTING).assertIsDisplayed()
        onNodeWithTag(CONTENT).performScrollToNode(hasTestTag(PERIODIC_WORK_SETTING))
        onNodeWithTag(PERIODIC_WORK_SETTING).assertIsDisplayed()
        onNodeWithTag(CONTENT).performScrollToNode(hasTestTag(TIPS))
        onNodeWithTag(TIPS).assertIsDisplayed()
    }

    @Test
    fun backgroundServicesPage_BackButtonClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            BackgroundServicesPage(
                onBack = {
                    clicked = true
                },
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(GO_BACK_BUTTON).performClick()

        assert(clicked) { "Back button should be clickable" }
    }
}
