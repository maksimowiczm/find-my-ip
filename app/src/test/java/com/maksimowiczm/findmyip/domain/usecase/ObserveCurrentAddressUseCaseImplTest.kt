package com.maksimowiczm.findmyip.domain.usecase

import app.cash.turbine.test
import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coEvery
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
    fun `ObserveCurrentAddressUseCaseImpl proxy to AddressObserver`() = runTest {
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = mockk(relaxed = true),
            handleNewAddressUseCase = mockk(relaxed = true)
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
    fun `ObserveCurrentAddressUseCaseImpl inserts address to local data source`() = runTest {
        val networkAddress = testNetworkAddress()
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver> {
            every { flow } returns addressFlow.filterNotNull()
        }
        val insertNetworkAddressIfChangedUseCase =
            mockk<InsertNetworkAddressIfChangedUseCase>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = mockk(relaxed = true)
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Success(networkAddress))
            awaitItem()
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 1
        ) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(networkAddress)
        }
    }

    @Test
    fun `ObserveCurrentAddressUseCaseImpl call HandleNewAddressUseCase on new address`() = runTest {
        val networkAddress = testNetworkAddress()
        val domainAddress = testAddress()

        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }
        val insertNetworkAddressIfChangedUseCase = mockk<InsertNetworkAddressIfChangedUseCase> {
            coEvery { insertNetworkAddressIfChanged(networkAddress) } returns domainAddress
        }
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Success(networkAddress))
            awaitItem()

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 1
        ) {
            handleNewAddressUseCase.handle(domainAddress)
        }
    }

    @Test
    fun `ObserveCurrentAddressUseCaseImpl does not call HandleNewAddressUseCase on same address`() =
        runTest {
            val addressFlow = MutableStateFlow<AddressState?>(null)
            val addressObserver = mockk<AddressObserver>(relaxed = true) {
                every { flow } returns addressFlow.filterNotNull()
            }
            val insertNetworkAddressIfChangedUseCase =
                mockk<InsertNetworkAddressIfChangedUseCase>(relaxed = true) {
                    coEvery { insertNetworkAddressIfChanged(testNetworkAddress()) } returns null
                }
            val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

            val useCase = ObserveCurrentAddressUseCaseImpl(
                addressObserver = addressObserver,
                insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
                handleNewAddressUseCase = handleNewAddressUseCase
            )

            useCase.observe().test {
                addressFlow.emit(AddressState.Success(testNetworkAddress()))
                awaitItem()

                cancelAndConsumeRemainingEvents()
            }

            coVerify(
                exactly = 0
            ) {
                handleNewAddressUseCase.handle(any())
            }
        }
}
