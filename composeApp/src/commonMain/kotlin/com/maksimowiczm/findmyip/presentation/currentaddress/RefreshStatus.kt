package com.maksimowiczm.findmyip.presentation.currentaddress

sealed interface RefreshStatus {
    data object Idle : RefreshStatus

    data object Refreshing : RefreshStatus

    data class Error(val message: String?) : RefreshStatus
}
