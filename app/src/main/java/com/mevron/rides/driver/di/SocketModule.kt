package com.mevron.rides.driver.di

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.service.SocketManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideSocketManager(preferenceRepository: IPreferenceRepository): ISocketManager =
        SocketManager(preferenceRepository)
}