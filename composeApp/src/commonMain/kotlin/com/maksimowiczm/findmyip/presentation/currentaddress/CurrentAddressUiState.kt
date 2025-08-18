package com.maksimowiczm.findmyip.presentation.currentaddress

import androidx.compose.runtime.Immutable

@Immutable
sealed interface CurrentAddressUiState {
    val ip4: String?
    val isError: Boolean
    val isLoading: Boolean

    @Immutable
    data class Loading(override val ip4: String?) : CurrentAddressUiState {
        override val isError: Boolean = false
        override val isLoading: Boolean = true
    }

    @Immutable
    data class Success(override val ip4: String) : CurrentAddressUiState {
        override val isError: Boolean = false
        override val isLoading: Boolean = false
    }

    @Immutable
    data class Error(override val ip4: String?) : CurrentAddressUiState {
        override val isError: Boolean = true
        override val isLoading: Boolean = false
    }
}
