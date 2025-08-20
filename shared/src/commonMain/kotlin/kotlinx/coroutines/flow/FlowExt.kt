package kotlinx.coroutines.flow

fun <T : Any> Flow<T>.observeWithInitialNull(): Flow<T?> = (this as Flow<T?>).onStart { emit(null) }
