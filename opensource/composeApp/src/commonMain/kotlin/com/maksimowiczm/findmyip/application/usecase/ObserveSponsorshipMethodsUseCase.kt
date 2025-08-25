package com.maksimowiczm.findmyip.application.usecase

import com.maksimowiczm.findmyip.domain.SponsorshipMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun interface ObserveSponsorshipMethodsUseCase {
    fun observe(): Flow<List<SponsorshipMethod>>
}

internal class ObserveSponsorshipMethodsUseCaseImpl() : ObserveSponsorshipMethodsUseCase {
    override fun observe(): Flow<List<SponsorshipMethod>> = flowOf(SponsorshipMethod.methods)
}
