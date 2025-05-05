package com.maksimowiczm.findmyip.ui.page.history

import app.cash.turbine.test
import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.repository.AddressRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.Test

class HistoryPageViewModelTest {

    private val emptyListRepository: AddressRepository
        get() = mockk<AddressRepository> {
            every {
                observeAddresses(
                    query = any(),
                    internetProtocolFilters = any(),
                    networkTypeFilters = any(),
                    startDate = any(),
                    endDate = any()
                )
            } returns flowOf(emptyList())
        }

    @Test
    fun `Initial State Check`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        viewModel.state.test {
            assertEquals(
                HistoryPageState(),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Show and Hide Search Intent`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        viewModel.state.test {
            skipItems(1) // Skip the initial state emission

            viewModel.onIntent(HistoryPageIntent.ShowSearch)

            assertEquals(
                HistoryPageState(showSearch = true),
                awaitItem()
            )

            viewModel.onIntent(HistoryPageIntent.HideSearch)

            assertEquals(
                HistoryPageState(showSearch = false),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Search Query Intent`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        viewModel.state.test {
            skipItems(1) // Skip the initial state emission

            viewModel.onIntent(HistoryPageIntent.Search("test"))

            assertEquals(
                HistoryPageState(searchQuery = "test"),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Filter By Protocols Intent`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        viewModel.state.test {
            skipItems(1) // Skip the initial state emission

            viewModel.onIntent(
                HistoryPageIntent.FilterByProtocols(
                    listOf(
                        InternetProtocol.IPv4,
                        InternetProtocol.IPv6
                    )
                )
            )

            assertEquals(
                HistoryPageState(
                    internetProtocolsFilters = listOf(
                        InternetProtocol.IPv4,
                        InternetProtocol.IPv6
                    )
                ),
                awaitItem()
            )

            viewModel.onIntent(HistoryPageIntent.FilterByProtocols(emptyList()))

            assertEquals(
                HistoryPageState(
                    internetProtocolsFilters = emptyList()
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Filter By Network Type Intent`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        viewModel.state.test {
            skipItems(1) // Skip the initial state emission

            viewModel.onIntent(
                HistoryPageIntent.FilterByNetworkType(
                    listOf(
                        NetworkType.WiFi,
                        NetworkType.Cellular
                    )
                )
            )

            assertEquals(
                HistoryPageState(
                    networkTypeFilters = listOf(
                        NetworkType.WiFi,
                        NetworkType.Cellular
                    )
                ),
                awaitItem()
            )

            viewModel.onIntent(HistoryPageIntent.FilterByNetworkType(emptyList()))

            assertEquals(
                HistoryPageState(
                    networkTypeFilters = emptyList()
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Filter By Date Intent`() = runTest {
        val viewModel = HistoryPageViewModel(emptyListRepository)

        val startDate = LocalDate(2025, 5, 3)
        val endDate = LocalDate(2025, 5, 10)

        viewModel.state.test {
            skipItems(1) // Skip the initial state emission

            viewModel.onIntent(HistoryPageIntent.FilterByDate(startDate, endDate))
            assertEquals(
                HistoryPageState(
                    dateRange = DateRange(startDate, endDate)
                ),
                awaitItem()
            )

            viewModel.onIntent(HistoryPageIntent.ClearDateFilter)
            assertEquals(
                HistoryPageState(
                    dateRange = null
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Delete Address Intent`() = runTest {
        val address = testAddress()
        val uiAddress = Address.fromDomain(address)

        val repository = mockk<AddressRepository>(relaxed = true) {
            every {
                observeAddresses(
                    query = any(),
                    internetProtocolFilters = any(),
                    networkTypeFilters = any(),
                    startDate = any(),
                    endDate = any()
                )
            } returns flowOf(listOf(address))
        }
        val viewModel = HistoryPageViewModel(repository)

        viewModel.onIntent(HistoryPageIntent.DeleteAddress(uiAddress))

        coVerify {
            repository.deleteAddress(address.id)
        }
    }
}
