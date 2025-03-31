@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

interface HistoryManager {
    /**
     * Observes the address for both IPv4 and IPv6 and saves them to the database.
     */
    suspend fun run()

    /**
     * Retrieves the address for both IPv4 and IPv6 and saves them to the database.
     */
    suspend fun once()
}
