package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPageTestTags.BACK_BUTTON
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPageTestTags.DISABLED_CONTENT
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPageTestTags.ENABLED_CONTENT
import com.maksimowiczm.findmyip.ui.page.settings.notifications.NotificationsPageTestTags.SYSTEM_NOTIFICATIONS_SETTINGS
import org.junit.Rule
import org.junit.Test

class NotificationsPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationsPage_Disabled(): Unit = composeTestRule.run {
        setContent {
            NotificationsPage(
                state = NotificationsPageState.Disabled,
                onIntent = {},
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(DISABLED_CONTENT).assertIsDisplayed()
        onNodeWithTag(ENABLED_CONTENT).assertDoesNotExist()
        onNodeWithTag(SYSTEM_NOTIFICATIONS_SETTINGS).assertDoesNotExist()
    }

    @Test
    fun notificationsPage_Enabled(): Unit = composeTestRule.run {
        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = {},
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(BACK_BUTTON).assertIsDisplayed()
        onNodeWithTag(DISABLED_CONTENT).assertDoesNotExist()
        onNodeWithTag(ENABLED_CONTENT)
            .assertIsDisplayed()
            .performScrollToNode(hasTestTag(SYSTEM_NOTIFICATIONS_SETTINGS))
        onNodeWithTag(SYSTEM_NOTIFICATIONS_SETTINGS).assertIsDisplayed()
    }

    @Test
    fun notificationsPage_BackClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = {},
                onBack = { clicked = true },
                onSystemSettings = {}
            )
        }

        onNodeWithTag(BACK_BUTTON).performClick()

        assert(clicked)
    }

    @Test
    fun notificationsPage_SystemSettingsClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = {},
                onBack = {},
                onSystemSettings = { clicked = true }
            )
        }

        onNodeWithTag(ENABLED_CONTENT)
            .performScrollToNode(hasTestTag(SYSTEM_NOTIFICATIONS_SETTINGS))
        onNodeWithTag(SYSTEM_NOTIFICATIONS_SETTINGS).performClick()

        assert(clicked)
    }
}
