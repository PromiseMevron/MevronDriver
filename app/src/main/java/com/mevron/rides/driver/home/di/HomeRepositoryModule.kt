package com.mevron.rides.driver.home.di

import com.mevron.rides.driver.home.data.network.HomeScreenApi
import com.mevron.rides.driver.home.data.repository.HomeScreenRepository
import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import com.mevron.rides.driver.util.Constants.MEVRON_CALL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object HomeRepositoryModule {

    @Provides
    @Singleton
    fun provideHomeApi(@Named(MEVRON_CALL) retrofit: Retrofit): HomeScreenApi =
        retrofit.create(HomeScreenApi::class.java)

    @Provides
    @Singleton
    fun provideHomeScreenRepository(api: HomeScreenApi): IHomeScreenRepository =
        HomeScreenRepository(api)
}