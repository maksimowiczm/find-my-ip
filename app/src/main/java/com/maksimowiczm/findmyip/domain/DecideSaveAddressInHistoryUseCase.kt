package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.NetworkType
import com.maksimowiczm.findmyip.data.repository.UserPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class DecideSaveAddressInHistoryUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Decides whether the given address should be saved in the history.
     */
    suspend operator fun invoke(address: Address): Boolean {
        if (address.networkType == null) {
            return false
        }

        if (userPreferencesRepository.get(Keys.save_history).first() != true) {
            return false
        }

        return when (address.networkType) {
            NetworkType.WIFI ->
                userPreferencesRepository
                    .get(Keys.save_wifi_history)
                    .first() == true

            NetworkType.MOBILE ->
                userPreferencesRepository
                    .get(Keys.save_mobile_history)
                    .first() == true

            NetworkType.VPN ->
                userPreferencesRepository
                    .get(Keys.save_vpn_history)
                    .first() == true
        }
    }
}
