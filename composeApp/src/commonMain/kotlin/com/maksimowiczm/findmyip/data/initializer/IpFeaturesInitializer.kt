package com.maksimowiczm.findmyip.data.initializer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.set
import com.maksimowiczm.findmyip.network.NetworkAddressDataSource
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout

class IpFeaturesInitializer(
    private val dataStore: DataStore<Preferences>,
    private val ipv4Source: NetworkAddressDataSource,
    private val ipv6Source: NetworkAddressDataSource
) : Initializer {
    override suspend fun invoke() {
        val tested = dataStore.get(PreferenceKeys.ipFeaturesTested)

        if (tested == true) {
            return
        }

        coroutineScope {
            val (ipv4test, ipv6test) = awaitAll(
                async {
                    try {
                        withTimeout(5_000) {
                            ipv4Source.refreshAddress()
                        }
                    } catch (e: TimeoutCancellationException) {
                        Result.failure(e)
                    }
                },
                async {
                    try {
                        withTimeout(5_000) {
                            ipv6Source.refreshAddress()
                        }
                    } catch (e: TimeoutCancellationException) {
                        Result.failure(e)
                    }
                }
            )

            // If both test fail set IPv4 as default.
            val ipv4 = if (ipv4test.isFailure && ipv6test.isFailure) {
                true
            } else {
                ipv4test.isSuccess
            }
            val ipv6 = ipv6test.isSuccess

            dataStore.set(
                PreferenceKeys.ipv4Enabled to ipv4,
                PreferenceKeys.ipv6Enabled to ipv6,
                PreferenceKeys.ipFeaturesTested to true
            )
        }
    }
}
