package com.maksimowiczm.findmyip.ui.page.history

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test

class HistoryPageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun historyPage_testInitial(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.SHOW_SEARCHBAR_ICON_BUTTON).assertIsDisplayed()
        onNodeWithTag(HistoryPageTestTag.HIDE_SEARCHBAR_ICON_BUTTON).assertDoesNotExist()
        onNodeWithTag(HistoryPageTestTag.SEARCHBAR).assertDoesNotExist()
        onNodeWithTag(HistoryPageTestTag.INTERNET_PROTOCOL_FILTER_CHIP).assertDoesNotExist()
        onNodeWithTag(HistoryPageTestTag.NETWORK_TYPE_FILTER_CHIP).assertDoesNotExist()
        onNodeWithTag(HistoryPageTestTag.DATE_FILTER_CHIP).assertDoesNotExist()
    }

    @Test
    fun historyPage_showSearchBar(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(
                    showSearch = true
                ),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.SHOW_SEARCHBAR_ICON_BUTTON).assertDoesNotExist()
        onNodeWithTag(HistoryPageTestTag.HIDE_SEARCHBAR_ICON_BUTTON).assertIsDisplayed()
        onNodeWithTag(HistoryPageTestTag.SEARCHBAR).assertIsDisplayed()
        onNodeWithTag(HistoryPageTestTag.INTERNET_PROTOCOL_FILTER_CHIP).assertIsDisplayed()
        onNodeWithTag(HistoryPageTestTag.NETWORK_TYPE_FILTER_CHIP).assertIsDisplayed()
        onNodeWithTag(HistoryPageTestTag.DATE_FILTER_CHIP).assertIsDisplayed()
    }

    @Test
    fun historyPage_internetProtocolFilterSelected(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(
                    showSearch = true,
                    internetProtocolsFilters = listOf(InternetProtocol.IPv4)
                ),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.INTERNET_PROTOCOL_FILTER_CHIP)
            .assertIsDisplayed()
            .assertIsSelected()
    }

    @Test
    fun historyPage_networkTypeFilterSelected(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(
                    showSearch = true,
                    networkTypeFilters = listOf(NetworkType.WiFi)
                ),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.NETWORK_TYPE_FILTER_CHIP)
            .assertIsDisplayed()
            .assertIsSelected()
    }

    @Test
    fun historyPage_dateFilterSelected(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(
                    showSearch = true,
                    dateRange = DateRange(
                        start = LocalDate(2025, 1, 1),
                        end = LocalDate(2025, 12, 31)
                    )
                ),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.DATE_FILTER_CHIP)
            .assertIsDisplayed()
            .assertIsSelected()
    }

    @Test
    fun historyPage_addressListDisplayed(): Unit = composeTestRule.run {
        setContent {
            HistoryPage(
                state = HistoryPageState(
                    addressList = listOf(
                        Address(
                            ip = "192.168.1.1",
                            dateTime = LocalDateTime(2023, 10, 1, 12, 0),
                            networkType = NetworkType.WiFi
                        )
                    )
                ),
                onIntent = {}
            )
        }

        onNodeWithTag(HistoryPageTestTag.ADDRESS_LIST).assertIsDisplayed()
    }
}
