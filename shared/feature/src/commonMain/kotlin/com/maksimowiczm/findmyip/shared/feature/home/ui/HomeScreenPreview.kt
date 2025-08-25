package com.maksimowiczm.findmyip.shared.feature.home.ui

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.maksimowiczm.findmyip.shared.core.feature.ui.FindMyIpTheme
import com.maksimowiczm.findmyip.shared.core.infrastructure.fake.CommonAddresses
import com.maksimowiczm.findmyip.shared.feature.home.persentation.AddressHistoryUiModel
import com.maksimowiczm.findmyip.shared.feature.home.persentation.CurrentAddressUiModel
import com.maksimowiczm.findmyip.shared.feature.home.persentation.Filter
import com.maksimowiczm.findmyip.shared.feature.home.persentation.InternetProtocolVersion
import com.maksimowiczm.findmyip.shared.feature.home.persentation.NetworkType
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreenPreview() {
    val now = LocalDateTime(2025, 8, 22, 15, 34, 11)
    val ip4 =
        CurrentAddressUiModel.Address(
            address = CommonAddresses.GOOGLE_V4_1,
            domain = "google.com",
            dateTime = now,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPV4,
        )

    val ip6 =
        CurrentAddressUiModel.Address(
            address = CommonAddresses.GOOGLE_V6,
            domain = "google.com",
            dateTime = now,
            networkType = NetworkType.WIFI,
            internetProtocolVersion = InternetProtocolVersion.IPV6,
        )

    val history =
        flowOf(
                PagingData.Companion.from(
                    listOf(
                            AddressHistoryUiModel(
                                id = 1,
                                address = CommonAddresses.GITHUB_V4_1,
                                domain = null,
                                networkType = NetworkType.WIFI,
                                dateTime = now.minus(1.hours + 1.minutes + 43.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 2,
                                address = CommonAddresses.GITHUB_V4_2,
                                networkType = NetworkType.CELLULAR,
                                domain = null,
                                dateTime = now.minus(2.hours + 5.minutes + 29.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 3,
                                address = CommonAddresses.GOOGLE_V6,
                                networkType = NetworkType.UNKNOWN,
                                domain = "google.com",
                                dateTime = now.minus(5.hours + 12.minutes + 10.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                            AddressHistoryUiModel(
                                id = 4,
                                address = CommonAddresses.CHATGPT_V6,
                                networkType = NetworkType.VPN,
                                domain = null,
                                dateTime = now.minus(1.days + 6.hours + 22.minutes + 30.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                            AddressHistoryUiModel(
                                id = 5,
                                address = CommonAddresses.CHATGPT_V4_1,
                                networkType = NetworkType.WIFI,
                                domain = null,
                                dateTime = now.minus(3.days + 2.hours + 15.minutes + 5.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 6,
                                address = CommonAddresses.CHATGPT_V4_2,
                                networkType = NetworkType.WIFI,
                                domain = null,
                                dateTime = now.minus(3.days + 2.hours + 10.minutes + 22.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV4,
                            ),
                            AddressHistoryUiModel(
                                id = 7,
                                address = CommonAddresses.ANDROID_V6,
                                networkType = NetworkType.CELLULAR,
                                domain = null,
                                dateTime = now.minus(3.days + 5.hours + 45.minutes + 32.seconds),
                                internetProtocolVersion = InternetProtocolVersion.IPV6,
                            ),
                        )
                        .sortedByDescending { it.dateTime }
                )
            )
            .collectAsLazyPagingItems()

    FindMyIpTheme {
        HomeScreen(
            ip4 = ip4,
            ip6 = ip6,
            history = history,
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
