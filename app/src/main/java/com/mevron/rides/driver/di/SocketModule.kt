package com.mevron.rides.driver.di

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.service.SocketManager
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository.IPrefrenceRepository
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
    fun provideSocketManager(mapRepository: IMapStateRepository, iPrefrenceRepository: IPreferenceRepository): ISocketManager =
        SocketManager(mapRepository, iPrefrenceRepository)
}