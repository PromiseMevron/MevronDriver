package com.mevron.rides.driver.remote.geoservice.di

import com.mevron.rides.driver.remote.geoservice.data.network.GeoAPIInterface
import com.mevron.rides.driver.remote.geoservice.data.repository.GoogleRepository
import com.mevron.rides.driver.remote.geoservice.domain.repository.IGoogleRepository
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

object GoogleApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(@Named("googleCalls") retrofit: Retrofit) =
        retrofit.create<GeoAPIInterface>()

    @Provides
    @Singleton
    fun provideGoogleRepository(api: GeoAPIInterface): IGoogleRepository = GoogleRepository(api)
}