package com.maksimowiczm.findmyip.ui.page.home

import app.cash.turbine.test
import com.maksimowiczm.findmyip.domain.source.Address
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomePageViewModelTest {

    @Test
    fun `Initial state check`() = runTest {
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns flow { }

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)

        viewModel.state.test {
            assertEquals(HomePageState(), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial into loading state`() = runTest {
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns flow { emit(AddressState.Refreshing) }

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)

        viewModel.state.test {
            assertEquals(
                HomePageState(
                    ipv4 = IpState.Loading(null),
                    ipv6 = IpState.Loading(null)
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial into error state`() = runTest {
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns flow { emit(AddressState.Error(null)) }

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)

        viewModel.state.test {
            assertEquals(HomePageState(), awaitItem())

            assertEquals(
                HomePageState(
                    noInternetConnection = true,
                    ipv4 = IpState.NotDetected,
                    ipv6 = IpState.NotDetected
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Initial into success state`() = runTest {
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns flow { emit(AddressState.Success(Address("ip"))) }

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)
        viewModel.state.test {
            assertEquals(HomePageState(), awaitItem())

            assertEquals(
                HomePageState(
                    ipv4 = IpState.Success("ip"),
                    ipv6 = IpState.Success("ip")
                ),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Preserve ip on refreshing state`() = runTest {
        val addressStateFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns addressStateFlow.filterNotNull()

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)

        viewModel.state.test {
            // Initial state
            assertEquals(HomePageState(), awaitItem())

            launch {
                addressStateFlow.emit(AddressState.Success(Address("initial")))
            }

            // After first success
            val state1 = awaitItem()
            assertEquals(IpState.Success("initial"), state1.ipv4)

            launch {
                addressStateFlow.emit(AddressState.Refreshing)
            }

            // Refreshing
            val state2 = awaitItem()
            assertEquals(IpState.Loading("initial"), state2.ipv4)

            launch {
                addressStateFlow.emit(AddressState.Success(Address("new")))
            }

            // Final Success (new address)
            val state3 = awaitItem()
            assertEquals(IpState.Success("new"), state3.ipv4)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Success into error state`() = runTest {
        val addressStateFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>()
        every { addressObserver.flow } returns addressStateFlow.filterNotNull()

        val viewModel = HomePageViewModel(addressObserver, addressObserver, loadingDelayMillis = 0)

        viewModel.state.test {
            // Initial state
            assertEquals(HomePageState(), awaitItem())

            launch {
                addressStateFlow.emit(AddressState.Success(Address("initial")))
            }

            // After first success
            val state1 = awaitItem()
            assertEquals(IpState.Success("initial"), state1.ipv4)

            launch {
                addressStateFlow.emit(AddressState.Error(null))
            }

            // Error
            val state2 = awaitItem()
            assertEquals(IpState.NotDetected, state2.ipv4)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Call refresh on refresh`() = runTest {
        val addressObserver4 = mockk<AddressObserver>(relaxed = true)
        val addressObserver6 = mockk<AddressObserver>(relaxed = true)
        every { addressObserver4.flow } returns flowOf()
        every { addressObserver6.flow } returns flowOf()

        val viewModel = HomePageViewModel(addressObserver4, addressObserver6)
        viewModel.onRefresh()

        verify(
            exactly = 1
        ) {
            runBlocking { addressObserver4.refresh() }
        }

        verify(
            exactly = 1
        ) {
            runBlocking { addressObserver6.refresh() }
        }
    }
}
