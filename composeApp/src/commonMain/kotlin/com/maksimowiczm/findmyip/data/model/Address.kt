package com.maksimowiczm.findmyip.data.model

sealed interface Address {
    data object Loading : Address
    data object Disabled : Address
    data class Success(val ip: String) : Address
    data class Error(val message: String) : Address
}
