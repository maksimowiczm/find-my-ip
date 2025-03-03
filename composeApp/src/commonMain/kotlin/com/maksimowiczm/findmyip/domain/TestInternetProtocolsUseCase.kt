package com.maksimowiczm.findmyip.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.maksimowiczm.findmyip.data.AddressRepository
import com.maksimowiczm.findmyip.data.PreferenceKeys
import com.maksimowiczm.findmyip.data.model.InternetProtocolVersion
import com.maksimowiczm.findmyip.infrastructure.di.get
import com.maksimowiczm.findmyip.infrastructure.di.set
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout

// TODO
//  This will fail because address repository will return disabled by default
class TestInternetProtocolsUseCase(
    private val dataStore: DataStore<Preferences>,
    private val addressRepository: AddressRepository
) {
    suspend operator fun invoke() {
        val tested = dataStore.get(PreferenceKeys.ipFeaturesTested)

        if (tested == true) {
            return
        }

        coroutineScope {
            val (ipv4test, ipv6test) = awaitAll(
                async {
                    runCatching {
                        withTimeout(5_000) {
                            addressRepository.observeAddress(InternetProtocolVersion.IPv4)
                        }.first()
                    }
                },
                async {
                    runCatching {
                        withTimeout(5_000) {
                            addressRepository.observeAddress(InternetProtocolVersion.IPv6)
                        }.first()
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
