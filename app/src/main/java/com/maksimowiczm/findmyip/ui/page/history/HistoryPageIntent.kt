package com.maksimowiczm.findmyip.ui.page.history

import com.maksimowiczm.findmyip.domain.model.InternetProtocol
import com.maksimowiczm.findmyip.domain.model.NetworkType
import kotlinx.datetime.LocalDate

sealed interface HistoryPageIntent {
    data object ShowSearch : HistoryPageIntent
    data object HideSearch : HistoryPageIntent

    data class SearchQueryChanged(val query: String) : HistoryPageIntent
    data object ClearSearch : HistoryPageIntent
    data class Search(val query: String) : HistoryPageIntent

    data class DeleteAddress(val address: Address) : HistoryPageIntent

    data class FilterByDate(val start: LocalDate, val end: LocalDate) : HistoryPageIntent
    data object ClearDateFilter : HistoryPageIntent

    data class FilterByProtocols(val protocols: List<InternetProtocol>) : HistoryPageIntent

    data class FilterByNetworkType(val networkTypes: List<NetworkType>) : HistoryPageIntent
}
