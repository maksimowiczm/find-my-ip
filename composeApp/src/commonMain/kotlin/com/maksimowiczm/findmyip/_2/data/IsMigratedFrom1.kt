@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data

/**
 * This interface is used to check if the app has been migrated from version 1.
 */
fun interface IsMigratedFrom1 {
    operator fun invoke(): Boolean
}
