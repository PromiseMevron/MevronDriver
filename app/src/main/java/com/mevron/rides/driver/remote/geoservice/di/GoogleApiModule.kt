package com.mevron.rides.driver.remote.geoservice.di

import com.mevron.rides.driver.remote.geoservice.data.network.GeoAPIInterface
import com.mevron.rides.driver.remote.geoservice.data.repository.GoogleRepository
import com.mevron.rides.driver.remote.geoservice.domain.repository.IGoogleRepository
import com.mevron.rides.driver.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(@Named(Constants.GOOGLE_CALL) retrofit: Retrofit) =
        retrofit.create<GeoAPIInterface>()

    @Provides
    @Singleton
    fun provideGoogleRepository(api: GeoAPIInterface): IGoogleRepository = GoogleRepository(api)
}