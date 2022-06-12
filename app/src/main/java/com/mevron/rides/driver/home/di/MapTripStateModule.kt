package com.mevron.rides.driver.home.di

import com.mevron.rides.driver.home.data.repository.MapStateRepository
import com.mevron.rides.driver.home.domain.IMapStateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapTripStateModule {

    @Provides
    @Singleton
    fun provideTripStateRepository(): IMapStateRepository = MapStateRepository()
}