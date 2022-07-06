package com.mevron.rides.driver.updateprofile.di

import com.mevron.rides.driver.updateprofile.data.network.CarPropertiesApi
import com.mevron.rides.driver.updateprofile.data.repository.CarPropertiesRepository
import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import com.mevron.rides.driver.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object CarPropertiesModule {

    @Provides
    @Singleton
    fun provideCarPropertiesApi(@Named(Constants.MEVRON_CALL) retrofit: Retrofit): CarPropertiesApi =
        retrofit.create(CarPropertiesApi::class.java)

    @Provides
    @Singleton
    fun provideCarPropertiesRepository(api: CarPropertiesApi): ICarPropertiesRepository =
        CarPropertiesRepository(api)
}