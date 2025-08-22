package com.maksimowiczm.findmyip.screenshot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.maksimowiczm.findmyip.infrastructure.fake.CommonAddresses
import com.maksimowiczm.findmyip.presentation.home.AddressHistoryUiModel
import com.maksimowiczm.findmyip.presentation.home.CurrentAddressUiModel
import com.maksimowiczm.findmyip.presentation.home.Filter
import com.maksimowiczm.findmyip.presentation.home.InternetProtocolVersion
import com.maksimowiczm.findmyip.ui.home.HomeScreen
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlinx.datetime.now

data object HomeScreenScreenshot : Screenshot {
    override val name: String = "1"

    @Composable
    override fun Content() {
        val history = remember {
            flowOf(
                PagingData.from(
                    listOf(
                        AddressHistoryUiModel(
                            id = 1,
                            address = CommonAddresses.GITHUB_V4,
                            domain = null,
                            dateTime = LocalDateTime.now().minus(1.hours + 1.minutes),
                            internetProtocolVersion = InternetProtocolVersion.IPV4,
                        ),
                        AddressHistoryUiModel(
                            id = 2,
                            address = CommonAddresses.CHATGPT_V4,
                            domain = null,
                            dateTime = LocalDateTime.now().minus(2.hours + 5.minutes),
                            internetProtocolVersion = InternetProtocolVersion.IPV4,
                        ),
                        AddressHistoryUiModel(
                            id = 3,
                            address = CommonAddresses.GOOGLE_V6,
                            domain = "google.com",
                            dateTime = LocalDateTime.now().minus(5.hours + 12.minutes + 10.seconds),
                            internetProtocolVersion = InternetProtocolVersion.IPV6,
                        ),
                        AddressHistoryUiModel(
                            id = 4,
                            address = CommonAddresses.CHATGPT_V6,
                            domain = null,
                            dateTime =
                                LocalDateTime.now()
                                    .minus(1.days + 6.hours + 22.minutes + 30.seconds),
                            internetProtocolVersion = InternetProtocolVersion.IPV6,
                        ),
                    )
                )
            )
        }

        HomeScreen(
            ip4 =
                CurrentAddressUiModel.Address(
                    address = CommonAddresses.GOOGLE_V4,
                    domain = "google.com",
                    dateTime = LocalDateTime.Companion.now(),
                    internetProtocolVersion = InternetProtocolVersion.IPV4,
                ),
            ip6 =
                CurrentAddressUiModel.Address(
                    address = CommonAddresses.GOOGLE_V6,
                    domain = "google.com",
                    dateTime = LocalDateTime.Companion.now(),
                    internetProtocolVersion = InternetProtocolVersion.IPV6,
                ),
            history = history.collectAsLazyPagingItems(),
            filter = Filter(setOf()),
            isRefreshing = false,
            onRefresh = {},
            onSearch = {},
            onVolunteer = {},
            onSettings = {},
            onFilterUpdate = {},
        )
    }
}
