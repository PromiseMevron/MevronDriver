package com.mevron.rides.driver.home.trip_management.di

import com.mevron.rides.driver.home.trip_management.data.network.TripManagementAPI
import com.mevron.rides.driver.home.trip_management.data.repository.TripManagementRepository
import com.mevron.rides.driver.home.trip_management.domain.repository.ITripManagementRepository
import com.mevron.rides.driver.util.Constants.MEVRON_CALL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TripManagementModule {

    @Provides
    @Singleton
    fun provideTripApi(@Named(MEVRON_CALL) retrofit: Retrofit): TripManagementAPI =
        retrofit.create(TripManagementAPI::class.java)

    @Provides
    @Singleton
    fun provideHomeScreenRepository(api: TripManagementAPI): ITripManagementRepository =
        TripManagementRepository(api)
}