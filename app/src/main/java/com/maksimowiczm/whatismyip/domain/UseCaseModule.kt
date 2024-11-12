package com.maksimowiczm.whatismyip.domain

import android.content.Context
import android.text.format.DateFormat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun provideFormatDateUseCase(@ApplicationContext context: Context): FormatDateUseCase {
        val format = DateFormat.getLongDateFormat(context)
        return FormatDateUseCase(format)
    }
}
