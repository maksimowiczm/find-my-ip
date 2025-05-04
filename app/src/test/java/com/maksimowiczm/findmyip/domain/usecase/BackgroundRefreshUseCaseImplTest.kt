package com.maksimowiczm.findmyip.domain.usecase

import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.testNetworkAddress
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BackgroundRefreshUseCaseImplTest {

    @Test
    fun `process on success`() = runTest {
        val networkAddress = testNetworkAddress()

        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.success(networkAddress)
        }
        val processAddressUseCase = mockk<ProcessAddressUseCase>()

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = processAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 1) {
            processAddressUseCase.process(networkAddress)
        }
    }

    @Test
    fun `shortcut on error`() = runTest {
        val addressObserver = mockk<AddressObserver> {
            coEvery { refresh() } returns Result.failure(Throwable())
        }
        val processAddressUseCase = mockk<ProcessAddressUseCase>()

        val backgroundRefreshUseCase = BackgroundRefreshUseCaseImpl(
            addressObserver = addressObserver,
            processAddressUseCase = processAddressUseCase
        )

        backgroundRefreshUseCase.refresh()

        coVerify(exactly = 1) {
            addressObserver.refresh()
        }

        coVerify(exactly = 0) {
            processAddressUseCase.process(any())
        }
    }
}
