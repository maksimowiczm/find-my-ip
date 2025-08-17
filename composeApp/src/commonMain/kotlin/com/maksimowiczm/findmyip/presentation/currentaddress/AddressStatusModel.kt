package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.compose.runtime.Immutable

@Immutable
sealed interface AddressStatusModel {
    data object Loading : AddressStatusModel

    data class Success(val ip4: String) : AddressStatusModel

    data object Error : AddressStatusModel
}
