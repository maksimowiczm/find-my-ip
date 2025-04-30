package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class NotificationsPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationsPage_disabled(): Unit = composeTestRule.run {
        setContent {
            NotificationsPage(
                state = NotificationsPageState.Disabled,
                onIntent = {},
                onBack = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH).assertIsDisplayed().assertIsOff()
        onNodeWithTag(NotificationsPageTestTags.DISABLED_CONTENT).assertIsDisplayed()
        onNodeWithTag(NotificationsPageTestTags.ENABLED_CONTENT).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_WIFI).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_CELLULAR).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_VPN).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV4).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV6).assertDoesNotExist()
    }

    @Test
    fun notificationsPage_enabled(): Unit = composeTestRule.run {
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
                onBack = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH).assertIsDisplayed().assertIsOn()
        onNodeWithTag(NotificationsPageTestTags.ENABLED_CONTENT).assertIsDisplayed()
        onNodeWithTag(NotificationsPageTestTags.DISABLED_CONTENT).assertDoesNotExist()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_WIFI).assertIsDisplayed().assertIsOn()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_CELLULAR).assertIsDisplayed().assertIsOn()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_VPN).assertIsDisplayed().assertIsOn()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV4).assertIsDisplayed().assertIsOn()
        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV6).assertIsDisplayed().assertIsOn()
    }
}
