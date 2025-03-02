package com.maksimowiczm.findmyip.domain

import com.maksimowiczm.findmyip.data.Keys
import com.maksimowiczm.findmyip.data.UserPreferencesRepository
import com.maksimowiczm.findmyip.data.model.Address
import com.maksimowiczm.findmyip.data.model.NetworkType

class DecideSaveAddressInHistoryUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    /**
     * Decides whether the given address should be saved in the history.
     */
    operator fun invoke(address: Address): Boolean {
        if (address.networkType == null) {
            return false
        }

        if (userPreferencesRepository.get(Keys.save_history) != true) {
            return false
        }

        return when (address.networkType) {
            NetworkType.WIFI -> userPreferencesRepository.get(Keys.save_wifi_history) == true
            NetworkType.MOBILE -> userPreferencesRepository.get(Keys.save_mobile_history) == true
            NetworkType.VPN -> userPreferencesRepository.get(Keys.save_vpn_history) == true
        }
    }
}
