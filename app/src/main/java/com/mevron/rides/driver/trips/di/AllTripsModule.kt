package com.mevron.rides.driver.trips.di

import com.mevron.rides.driver.trips.data.network.TripsAPI
import com.mevron.rides.driver.trips.data.repositroy.GetTripsRepository
import com.mevron.rides.driver.trips.domain.repository.IGetTripsRepository
import com.mevron.rides.driver.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AllTripsModule {

    @Provides
    @Singleton
    fun provideTripApi(@Named(Constants.MEVRON_CALL) retrofit: Retrofit): TripsAPI =
        retrofit.create(TripsAPI::class.java)

    @Provides
    @Singleton
    fun provideHomeScreenRepository(api: TripsAPI): IGetTripsRepository =
        GetTripsRepository(api)
}