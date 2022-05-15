package com.mevron.rides.driver.di

import android.content.Context
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.location.data.AppLocationManager
import com.mevron.rides.driver.location.data.LocationRepository
import com.mevron.rides.driver.location.domain.repository.IAppLocationManager
import com.mevron.rides.driver.location.domain.repository.ILocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationRepositoryModule {

    @Provides
    @Singleton
    fun provideLocationManager(
        @ApplicationContext context: Context
    ): IAppLocationManager = AppLocationManager(context)

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationManager: IAppLocationManager,
        socketManager: ISocketManager
    ): ILocationRepository = LocationRepository(locationManager, socketManager)
}