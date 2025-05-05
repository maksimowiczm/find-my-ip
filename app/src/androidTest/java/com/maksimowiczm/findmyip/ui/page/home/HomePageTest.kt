package com.maksimowiczm.findmyip.ui.page.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

// This doesn't test all cases, but it is enough to check that the UI is working correctly. At least
// for now...
class HomePageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val ipv4 = "192.168.1.1"
    private val ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334"

    @Test
    fun homePage_LoadingShimmers(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Loading(null),
                    ipv6 = IpState.Loading(null)
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_LoadingNoShimmers(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Loading(ipv4),
                    ipv6 = IpState.Loading(ipv6)
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertTextEquals(ipv4)
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertTextEquals(ipv6)
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_NotDetected(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.NotDetected,
                    ipv6 = IpState.NotDetected
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertIsDisplayed()

        onNodeWithTag(HomePageTestTags.IPV6).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertIsDisplayed()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_Success(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Success(ipv4),
                    ipv6 = IpState.Success(ipv6)
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertTextEquals(ipv4)
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertTextEquals(ipv6)
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_Ipv4SuccessIpv6LoadingShimmer(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Success(ipv4),
                    ipv6 = IpState.Loading(null)
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertTextEquals(ipv4)
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_Ipv4SuccessIpv6Loading(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Success(ipv4),
                    ipv6 = IpState.Loading(ipv6)
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertTextEquals(ipv4)
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertTextEquals(ipv6)
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_Ipv4SuccessIpv6NotDetected(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    ipv4 = IpState.Success(ipv4),
                    ipv6 = IpState.NotDetected
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.IPV4).assertTextEquals(ipv4)
        onNodeWithTag(HomePageTestTags.IPV4_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV4_NOT_DETECTED).assertDoesNotExist()

        onNodeWithTag(HomePageTestTags.IPV6).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_SHIMMER).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.IPV6_NOT_DETECTED).assertIsDisplayed()

        onNodeWithTag(HomePageTestTags.PROGRESS_INDICATOR).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    // Test cases for error scenarios
    @Test
    fun homePage_NoInternetConnection(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    noInternetConnection = true
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.ERROR_CARD).assertIsDisplayed()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }

    @Test
    fun homePage_OK(): Unit = composeTestRule.run {
        setContent {
            HomePage(
                state = HomePageState(
                    noInternetConnection = false
                ),
                onRefresh = {}
            )
        }

        onNodeWithTag(HomePageTestTags.ERROR_CARD).assertDoesNotExist()
        onNodeWithTag(HomePageTestTags.REFRESH_BUTTON).assertIsDisplayed()
    }
}
