package com.maksimowiczm.findmyip.ui.page.settings.backgroundservices

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
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

        onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH).assertIsDisplayed()
        onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH).assertIsDisplayed()
        onNodeWithTag(BackgroundServicesPageTestTags.TIPS).assertIsDisplayed()
    }

    @Test
    fun backgroundServicesPage_ForegroundServiceEnabledPeriodicWorkDisabled(): Unit =
        composeTestRule.run {
            setContent {
                BackgroundServicesPage(
                    onBack = {},
                    foregroundServiceEnabled = true,
                    onToggleForegroundService = {},
                    periodicWorkEnabled = false,
                    onTogglePeriodicWork = {}
                )
            }

            onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH)
                .assertIsDisplayed()
                .assertIsOn()
            onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH)
                .assertIsDisplayed()
                .assertIsOff()
            onNodeWithTag(BackgroundServicesPageTestTags.TIPS).assertIsDisplayed()
        }

    @Test
    fun backgroundServicesPage_ForegroundServiceDisabledPeriodicWorkEnabled(): Unit =
        composeTestRule.run {
            setContent {
                BackgroundServicesPage(
                    onBack = {},
                    foregroundServiceEnabled = false,
                    onToggleForegroundService = {},
                    periodicWorkEnabled = true,
                    onTogglePeriodicWork = {}
                )
            }

            onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH)
                .assertIsDisplayed()
                .assertIsOff()
            onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH)
                .assertIsDisplayed()
                .assertIsOn()
            onNodeWithTag(BackgroundServicesPageTestTags.TIPS).assertIsDisplayed()
        }

    @Test
    fun backgroundServicesPage_ForegroundServiceEnabledPeriodicWorkEnabled(): Unit =
        composeTestRule.run {
            setContent {
                BackgroundServicesPage(
                    onBack = {},
                    foregroundServiceEnabled = true,
                    onToggleForegroundService = {},
                    periodicWorkEnabled = true,
                    onTogglePeriodicWork = {}
                )
            }

            onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH)
                .assertIsDisplayed()
                .assertIsOn()
            onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH)
                .assertIsDisplayed()
                .assertIsOn()
            onNodeWithTag(BackgroundServicesPageTestTags.TIPS).assertIsDisplayed()
        }

    @Test
    fun backgroundServicesPage_ForegroundServiceDisabledPeriodicWorkDisabled(): Unit =
        composeTestRule.run {
            setContent {
                BackgroundServicesPage(
                    onBack = {},
                    foregroundServiceEnabled = false,
                    onToggleForegroundService = {},
                    periodicWorkEnabled = false,
                    onTogglePeriodicWork = {}
                )
            }

            onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH)
                .assertIsDisplayed()
                .assertIsOff()
            onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH)
                .assertIsDisplayed()
                .assertIsOff()
            onNodeWithTag(BackgroundServicesPageTestTags.TIPS).assertIsDisplayed()
        }

    @Test
    fun backgroundServicesPage_ForegroundServiceEnabledSwitchClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = true,
                onToggleForegroundService = { newState = it },
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH).performClick()

        assert(newState == false) { "Foreground service switch should be off" }
    }

    @Test
    fun backgroundServicesPage_ForegroundServiceEnabledSurfaceClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = true,
                onToggleForegroundService = { newState = it },
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SURFACE).performClick()

        assert(newState == false) { "Foreground service switch should be off" }
    }

    @Test
    fun backgroundServicesPage_ForegroundServiceDisabledSwitchClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = { newState = it },
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SWITCH).performClick()

        assert(newState == true) { "Foreground service switch should be on" }
    }

    @Test
    fun backgroundServicesPage_ForegroundServiceDisabledSurfaceClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = { newState = it },
                periodicWorkEnabled = false,
                onTogglePeriodicWork = {}
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.FOREGROUND_SERVICE_SURFACE).performClick()

        assert(newState == true) { "Foreground service switch should be on" }
    }

    @Test
    fun backgroundServicesPage_PeriodicWorkEnabledSwitchClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = true,
                onTogglePeriodicWork = { newState = it }
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH).performClick()

        assert(newState == false) { "Periodic work switch should be off" }
    }

    @Test
    fun backgroundServicesPage_PeriodicWorkEnabledSurfaceClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = true,
                onTogglePeriodicWork = { newState = it }
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SURFACE).performClick()

        assert(newState == false) { "Periodic work switch should be off" }
    }

    @Test
    fun backgroundServicesPage_PeriodicWorkDisabledSwitchClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = false,
                onTogglePeriodicWork = { newState = it }
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SWITCH).performClick()

        assert(newState == true) { "Periodic work switch should be on" }
    }

    @Test
    fun backgroundServicesPage_PeriodicWorkDisabledSurfaceClick(): Unit = composeTestRule.run {
        var newState: Boolean? = null

        setContent {
            BackgroundServicesPage(
                onBack = {},
                foregroundServiceEnabled = false,
                onToggleForegroundService = {},
                periodicWorkEnabled = false,
                onTogglePeriodicWork = { newState = it }
            )
        }

        onNodeWithTag(BackgroundServicesPageTestTags.PERIODIC_WORK_SURFACE).performClick()

        assert(newState == true) { "Periodic work switch should be on" }
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

        onNodeWithTag(BackgroundServicesPageTestTags.GO_BACK_BUTTON).performClick()

        assert(clicked) { "Back button should be clickable" }
    }
}
