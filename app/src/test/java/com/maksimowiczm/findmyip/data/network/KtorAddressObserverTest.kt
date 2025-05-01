package com.maksimowiczm.findmyip.data.network

import app.cash.turbine.test
import com.maksimowiczm.findmyip.domain.source.Address
import com.maksimowiczm.findmyip.domain.source.AddressState
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpStatusCode
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

class KtorAddressObserverTest {

    private val ip = "127.0.0.1"

    private val httpEngine = MockEngine {
        respond(ip)
    }

    private val errorHttpEngine = MockEngine {
        respondError(HttpStatusCode.ServiceUnavailable)
    }

    private val observer: KtorAddressObserver
        get() = KtorAddressObserver(
            url = "",
            client = HttpClient(httpEngine)
        )

    private val errorObserver: KtorAddressObserver
        get() = KtorAddressObserver(
            url = "",
            client = HttpClient(errorHttpEngine)
        )

    @Test
    fun `Initial state is refreshed`() = runTest {
        observer.flow.test {
            assertEquals(AddressState.Refreshing, awaitItem())
            assertEquals(AddressState.Success(Address(ip)), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Refresh state`() = runTest {
        val observer = observer

        observer.flow.test {
            assertEquals(AddressState.Refreshing, awaitItem())
            assertEquals(AddressState.Success(Address(ip)), awaitItem())

            launch {
                observer.refresh()
            }

            assertEquals(AddressState.Refreshing, awaitItem())
            assertEquals(AddressState.Success(Address(ip)), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Error state`() = runTest {
        errorObserver.flow.test {
            assertEquals(AddressState.Refreshing, awaitItem())
            assert(awaitItem() is AddressState.Error)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
