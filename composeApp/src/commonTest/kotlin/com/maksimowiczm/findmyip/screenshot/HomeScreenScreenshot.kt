package com.maksimowiczm.findmyip.screenshot

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.maksimowiczm.findmyip.presentation.home.AddressHistoryUiModel
import com.maksimowiczm.findmyip.presentation.home.CurrentAddressUiModel
import com.maksimowiczm.findmyip.presentation.home.Filter
import com.maksimowiczm.findmyip.presentation.home.InternetProtocolVersion
import com.maksimowiczm.findmyip.ui.home.HomeScreen
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.now

data object HomeScreenScreenshot : Screenshot {
    override val name: String = "1"

    @Composable
    override fun Content() {
        HomeScreen(
            ip4 =
                CurrentAddressUiModel.Address(
                    address = "8.8.8.8",
                    domain = null,
                    dateTime = LocalDateTime.Companion.now(),
                    internetProtocolVersion = InternetProtocolVersion.IPV4,
                ),
            ip6 =
                CurrentAddressUiModel.Address(
                    address = "2001:4860:4860::8888",
                    domain = null,
                    dateTime = LocalDateTime.Companion.now(),
                    internetProtocolVersion = InternetProtocolVersion.IPV6,
                ),
            history =
                flowOf(PagingData.Companion.from(emptyList<AddressHistoryUiModel>()))
                    .collectAsLazyPagingItems(),
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
