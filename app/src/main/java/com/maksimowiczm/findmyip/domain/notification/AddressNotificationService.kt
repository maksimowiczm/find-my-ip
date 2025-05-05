package com.maksimowiczm.findmyip.domain.notification

import com.maksimowiczm.findmyip.domain.model.Address

fun interface AddressNotificationService {
    fun postAddress(address: Address)
}
