package com.maksimowiczm.findmyip.domain.usecase

import app.cash.turbine.test
import com.maksimowiczm.findmyip.data.model.testAddressEntity
import com.maksimowiczm.findmyip.domain.source.AddressLocalDataSource
import com.maksimowiczm.findmyip.domain.source.AddressObserver
import com.maksimowiczm.findmyip.domain.source.AddressState
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
        // Given
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }
        val addressLocalDataSource = mockk<AddressLocalDataSource>(relaxed = true)

        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            addressLocalDataSource = addressLocalDataSource
        )

        useCase.observe().test {
            addressFlow.emit(AddressState.Success(testAddressEntity()))
            assertEquals(
                AddressState.Success(testAddressEntity()),
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
        // Given
        val addressFlow = MutableStateFlow<AddressState?>(null)
        val addressObserver = mockk<AddressObserver>(relaxed = true) {
            every { flow } returns addressFlow.filterNotNull()
        }
        val addressLocalDataSource = mockk<AddressLocalDataSource>(relaxed = true)
        val useCase = ObserveCurrentAddressUseCaseImpl(
            addressObserver = addressObserver,
            addressLocalDataSource = addressLocalDataSource
        )
        val entity = testAddressEntity()

        useCase.observe().test {
            addressFlow.emit(AddressState.Refreshing)
            awaitItem()

            addressFlow.emit(AddressState.Success(entity))
            awaitItem()

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(
            exactly = 1
        ) {
            addressLocalDataSource.insertAddressIfUniqueToLast(entity)
        }
    }
}
