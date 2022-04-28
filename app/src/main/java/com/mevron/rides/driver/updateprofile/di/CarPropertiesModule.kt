package com.mevron.rides.driver.updateprofile.di

import com.mevron.rides.driver.updateprofile.data.network.CarPropertiesApi
import com.mevron.rides.driver.updateprofile.data.repository.CarPropertiesRepository
import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object CarPropertiesModule {

    @Provides
    @Singleton
    fun provideCarPropertiesApi(retrofit: Retrofit): CarPropertiesApi =
        retrofit.create(CarPropertiesApi::class.java)

    @Provides
    @Singleton
    fun provideCarPropertiesRepository(api: CarPropertiesApi): ICarPropertiesRepository =
        CarPropertiesRepository(api)
}