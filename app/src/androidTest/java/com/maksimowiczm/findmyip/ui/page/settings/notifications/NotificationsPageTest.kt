package com.maksimowiczm.findmyip.ui.page.settings.notifications

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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
                onBack = {},
                onSystemSettings = {}
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
                onBack = {},
                onSystemSettings = {}
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
                onBack = { clicked = true }
            )
        }

        onNodeWithTag(NotificationsPageTestTags.BACK_BUTTON).performClick()

        assert(clicked)
    }

    @Test
    fun notificationsPage_WifiSwitchClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH_WIFI).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleWifi(false))
    }

    @Test
    fun notificationsPage_WifiSurfaceClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SURFACE_WIFI).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleWifi(false))
    }

    @Test
    fun notificationsPage_CellularSwitchClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH_CELLULAR).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleCellular(false))
    }

    @Test
    fun notificationsPage_CellularSurfaceClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SURFACE_CELLULAR).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleCellular(false))
    }

    @Test
    fun notificationsPage_VpnSwitchClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH_VPN).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleVpn(false))
    }

    @Test
    fun notificationsPage_VpnSurfaceClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SURFACE_VPN).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleVpn(false))
    }

    @Test
    fun notificationsPage_Ipv4SwitchClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV4).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleIpv4(false))
    }

    @Test
    fun notificationsPage_Ipv4SurfaceClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SURFACE_IPV4).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleIpv4(false))
    }

    @Test
    fun notificationsPage_Ipv6SwitchClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SWITCH_IPV6).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleIpv6(false))
    }

    @Test
    fun notificationsPage_Ipv6SurfaceClick(): Unit = composeTestRule.run {
        var intents: List<NotificationsPageIntent> = emptyList()

        setContent {
            NotificationsPage(
                state = NotificationsPageState.Enabled(
                    wifiEnabled = true,
                    cellularEnabled = true,
                    vpnEnabled = true,
                    ipv4Enabled = true,
                    ipv6Enabled = true
                ),
                onIntent = { intents = intents + it },
                onBack = {},
                onSystemSettings = {}
            )
        }

        onNodeWithTag(NotificationsPageTestTags.SURFACE_IPV6).performClick()

        assert(intents.size == 1)
        assert(intents[0] == NotificationsPageIntent.ToggleIpv6(false))
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

        onNodeWithTag(
            NotificationsPageTestTags.SURFACE_SYSTEM_NOTIFICATIONS_SETTINGS
        ).performClick()

        assert(clicked)
    }
}
