package com.maksimowiczm.findmyip.ui.page.settings.language

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import org.junit.Rule
import org.junit.Test

class LanguagePageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun languagePage_BackButtonClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            LanguagePage(
                tag = null,
                onTag = {},
                onHelp = {},
                onBack = { clicked = true }
            )
        }

        onNodeWithTag(LanguagePageTestTags.BACK_BUTTON)
            .assertIsDisplayed()
            .performClick()

        assert(clicked)
    }

    @Test
    fun languagePage_HelpButtonClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            LanguagePage(
                tag = null,
                onTag = {},
                onHelp = { clicked = true },
                onBack = {}
            )
        }

        onNodeWithTag(LanguagePageTestTags.HELP_BUTTON)
            .assertIsDisplayed()
            .performClick()

        assert(clicked)
    }

    @Test
    fun languagePage_SystemLanguageSettingsButtonClick(): Unit = composeTestRule.run {
        var clicked = false

        setContent {
            LanguagePage(
                tag = null,
                onTag = {},
                onHelp = {},
                onBack = {},
                onSystemLanguageSettings = { clicked = true }
            )
        }

        onNodeWithTag(LanguagePageTestTags.LANGUAGE_LIST)
            .performScrollToNode(hasTestTag(LanguagePageTestTags.SYSTEM_LANGUAGE_SETTINGS))
        onNodeWithTag(LanguagePageTestTags.SYSTEM_LANGUAGE_SETTINGS).performClick()

        assert(clicked)
    }

    @Test
    fun languagePage_SystemLanguageClick(): Unit = composeTestRule.run {
        var clicked = mutableListOf<String?>()

        setContent {
            LanguagePage(
                tag = null,
                onTag = { clicked.add(it) },
                onHelp = {},
                onBack = {}
            )
        }

        val testTag = LanguagePageTestTags.Language(null).toString()

        onNodeWithTag(LanguagePageTestTags.LANGUAGE_LIST).performScrollToNode(hasTestTag(testTag))
        onNodeWithTag(testTag).performClick()

        assert(clicked.size == 1)
        assert(clicked[0] == null)
    }

    @Test
    fun languagePage_LanguagesClick(): Unit = composeTestRule.run {
        var clicked = mutableListOf<String?>()

        setContent {
            LanguagePage(
                tag = null,
                onTag = { clicked.add(it) },
                onHelp = {},
                onBack = {}
            )
        }

        languages.forEach { (_, translation) ->
            val tag = translation.tag
            val testTag = LanguagePageTestTags.Language(tag).toString()
            onNodeWithTag(LanguagePageTestTags.LANGUAGE_LIST)
                .performScrollToNode(hasTestTag(testTag))
            onNodeWithTag(testTag).performClick()
        }

        assert(clicked.size == languages.size)
        languages.forEach { (_, translation) ->
            assert(clicked.contains(translation.tag))
        }
    }
}
