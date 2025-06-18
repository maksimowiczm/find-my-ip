package com.maksimowiczm.findmyip.ui.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsToggleable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class SwitchSettingListItemTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSwitchSettingListItem(): Unit = composeTestRule.run {
        setContent {
            SwitchSettingListItem(
                headlineContent = {},
                checked = true,
                onCheckedChange = {}
            )
        }

        onNodeWithTag(SwitchSettingListItemTestTags.SURFACE).assertIsDisplayed()
    }

    @Test
    fun testSWitchSettingsListItem_StateOn(): Unit = composeTestRule.run {
        setContent {
            SwitchSettingListItem(
                headlineContent = {},
                checked = true,
                onCheckedChange = { }
            )
        }

        onNodeWithTag(SwitchSettingListItemTestTags.SURFACE)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsToggleable()
            .assertIsOn()
    }

    @Test
    fun testSwitchSettingsListItem_StateOff(): Unit = composeTestRule.run {
        setContent {
            SwitchSettingListItem(
                headlineContent = {},
                checked = false,
                onCheckedChange = {}
            )
        }

        onNodeWithTag(SwitchSettingListItemTestTags.SURFACE)
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsToggleable()
            .assertIsOff()
    }

    @Test
    fun testSwitchSettingsListItem_StateOnClick(): Unit = composeTestRule.run {
        var clickEvents = mutableListOf<Boolean>()

        setContent {
            SwitchSettingListItem(
                headlineContent = {},
                checked = true,
                onCheckedChange = { clickEvents.add(it) }
            )
        }

        onNodeWithTag(SwitchSettingListItemTestTags.SURFACE)
            .assertIsDisplayed()
            .performClick()

        assert(clickEvents.size == 1)
        assert(clickEvents[0] == false)
    }

    @Test
    fun testSwitchSettingsListItem_StateOffClick(): Unit = composeTestRule.run {
        var clickEvents = mutableListOf<Boolean>()

        setContent {
            SwitchSettingListItem(
                headlineContent = {},
                checked = false,
                onCheckedChange = { clickEvents.add(it) }
            )
        }

        onNodeWithTag(SwitchSettingListItemTestTags.SURFACE)
            .assertIsDisplayed()
            .performClick()

        assert(clickEvents.size == 1)
        assert(clickEvents[0] == true)
    }
}
