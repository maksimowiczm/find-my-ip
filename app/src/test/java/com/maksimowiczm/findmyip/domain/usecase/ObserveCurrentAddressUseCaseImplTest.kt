package com.maksimowiczm.findmyip.domain.usecase

import app.cash.turbine.test
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveCurrentAddressUseCaseImplTest {

    @Test
    fun `proxy to AddressObserver`() = runTest {
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = mockk(relaxed = true)
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Success(testNetworkAddress()))
            assertEquals(
                AddressState.Success(testNetworkAddress()),
                awaitItem()
            )

            addressFlow.emit(AddressState.Error(null))
            assertEquals(
                AddressState.Error(null),
                awaitItem()
            )

            addressFlow.emit(AddressState.Refreshing)
            assertEquals(
                AddressState.Refreshing,
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `process on success`() = runTest {
        val networkAddress = testNetworkAddress()
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver> {
            every { flow } returns addressFlow.filterNotNull()
        }
        val processAddressUseCase = mockk<ProcessAddressUseCase>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = processAddressUseCase
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Success(networkAddress))
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 1
        ) {
            processAddressUseCase.process(networkAddress)
        }
    }

    @Test
    fun `don't process on error`() = runTest {
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver> {
            every { flow } returns addressFlow.filterNotNull()
        }
        val processAddressUseCase = mockk<ProcessAddressUseCase>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = processAddressUseCase
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Error(null))
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 0
        ) {
            processAddressUseCase.process(any())
        }
    }

    @Test
    fun `don't process on refreshing`() = runTest {
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver> {
            every { flow } returns addressFlow.filterNotNull()
        }
        val processAddressUseCase = mockk<ProcessAddressUseCase>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = processAddressUseCase
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Refreshing)
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 0
        ) {
            processAddressUseCase.process(any())
        }
    }
}
