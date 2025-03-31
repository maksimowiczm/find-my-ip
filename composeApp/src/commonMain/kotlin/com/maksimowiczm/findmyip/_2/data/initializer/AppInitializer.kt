@file:Suppress("PackageName")

package com.maksimowiczm.findmyip._2.data.initializer

class AppInitializer(private val initializers: List<Initializer>) : Initializer {
    override suspend fun invoke() = initializers.forEach { it() }
}
