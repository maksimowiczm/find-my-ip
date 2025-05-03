package com.maksimowiczm.findmyip.domain.usecase

import app.cash.turbine.test
import com.maksimowiczm.findmyip.data.model.AddressEntity
import com.maksimowiczm.findmyip.domain.model.Address
import com.maksimowiczm.findmyip.domain.model.AddressId
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test

class ObserveCurrentAddressUseCaseImplTest {

    @Test
    fun `ObserveCurrentAddressUseCaseImpl proxy to AddressObserver`() = runTest {
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }
        val addressLocalDataSource = mockk<AddressLocalDataSource>(relaxed = true)

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            addressLocalDataSource = addressLocalDataSource,
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
        val addressEntity = AddressEntity(
            ip = networkAddress.ip,
            internetProtocol = networkAddress.internetProtocol,
            networkType = networkAddress.networkType,
            epochMillis = networkAddress.dateTime
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()
        )
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver> {
            every { flow } returns addressFlow.filterNotNull()
        }
        val addressLocalDataSource = mockk<AddressLocalDataSource>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            addressLocalDataSource = addressLocalDataSource,
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
            addressLocalDataSource.insertAddressIfUniqueToLast(addressEntity)
        }
    }

    @Test
    fun `ObserveCurrentAddressUseCaseImpl call HandleNewAddressUseCase on new address`() = runTest {
        val networkAddress = testNetworkAddress()
        val domainAddress = Address(
            id = AddressId(100L),
            ip = networkAddress.ip,
            internetProtocol = networkAddress.internetProtocol,
            networkType = networkAddress.networkType,
            dateTime = networkAddress.dateTime
        )

        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }
        val addressLocalDataSource = mockk<AddressLocalDataSource> {
            coEvery { insertAddressIfUniqueToLast(any()) } returns 100L
        }
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            addressLocalDataSource = addressLocalDataSource,
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
            val addressLocalDataSource = mockk<AddressLocalDataSource>(relaxed = true) {
                coEvery { insertAddressIfUniqueToLast(any()) } returns null
            }
            val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

            val useCase = ObserveCurrentAddressUseCaseImpl(
                addressObserver = addressObserver,
                addressLocalDataSource = addressLocalDataSource,
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
