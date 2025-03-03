package com.maksimowiczm.findmyip.data.initializer

class AppInitializer(private val initializers: List<Initializer>) : Initializer {
    override suspend fun invoke() = initializers.forEach { it() }
}
