package kotlinx.coroutines.flow

/**
 * Combines multiple flows into a single flow of an array of their values.
 */
inline fun <reified T> combine(vararg flows: Flow<T>): Flow<Array<T>> = combine(*flows) { it }
