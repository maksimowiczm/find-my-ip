package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.model.testAddress
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BackgroundRefreshUseCaseImplTest {

    @Test
    fun `insert on success`() = runTest {
        val networkAddress = testNetworkAddress()

        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.success(networkAddress)
        }
        val insertNetworkAddressIfChangedUseCase =
            mockk<InsertNetworkAddressIfChangedUseCase>(relaxed = true)
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 1) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(networkAddress)
        }
    }

    @Test
    fun `shortcut on error`() = runTest {
        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.failure(Throwable())
        }
        val insertNetworkAddressIfChangedUseCase = mockk<InsertNetworkAddressIfChangedUseCase>()
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>()

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 0) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(any())
        }

        coVerify(exactly = 0) {
            handleNewAddressUseCase.handle(any())
        }
    }

    @Test
    fun `handle new address on inserted`() = runTest {
        val networkAddress = testNetworkAddress()
        val address = testAddress()

        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.success(networkAddress)
        }
        val insertNetworkAddressIfChangedUseCase =
            mockk<InsertNetworkAddressIfChangedUseCase> {
                coEvery { insertNetworkAddressIfChanged(any()) } returns address
            }
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>(relaxed = true)

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 1) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(networkAddress)
        }

        coVerify(exactly = 1) {
            handleNewAddressUseCase.handle(address)
        }
    }

    @Test
    fun `don't handle new address on not inserted`() = runTest {
        val networkAddress = testNetworkAddress()

        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.success(networkAddress)
        }
        val insertNetworkAddressIfChangedUseCase = mockk<InsertNetworkAddressIfChangedUseCase> {
            coEvery { insertNetworkAddressIfChanged(any()) } returns null
        }
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>()

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 1) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(networkAddress)
        }

        coVerify(exactly = 0) {
            handleNewAddressUseCase.handle(any())
        }
    }

    @Test
    fun `error on refresh`() = runTest {
        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.failure(Throwable())
        }
        val insertNetworkAddressIfChangedUseCase = mockk<InsertNetworkAddressIfChangedUseCase>()
        val handleNewAddressUseCase = mockk<HandleNewAddressUseCase>()

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            insertNetworkAddressIfChangedUseCase = insertNetworkAddressIfChangedUseCase,
            handleNewAddressUseCase = handleNewAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 0) {
            insertNetworkAddressIfChangedUseCase.insertNetworkAddressIfChanged(any())
        }

        coVerify(exactly = 0) {
            handleNewAddressUseCase.handle(any())
        }
    }
}
