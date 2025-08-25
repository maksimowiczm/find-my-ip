package com.maksimowiczm.findmyip.feature.sponsor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maksimowiczm.findmyip.application.usecase.ObserveSponsorshipMethodsUseCase
import org.koin.compose.koinInject

@Composable
fun SponsorRoute(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val observeSponsorshipMethodsUseCase: ObserveSponsorshipMethodsUseCase = koinInject()
    val methods by
        observeSponsorshipMethodsUseCase.observe().collectAsStateWithLifecycle(emptyList())

    SponsorScreen(onBack = onBack, methods = methods, modifier = modifier)
}
