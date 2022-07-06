package com.mevron.rides.driver.updateprofile.di

import com.mevron.rides.driver.updateprofile.data.network.UpdateProfileApi
import com.mevron.rides.driver.updateprofile.data.repository.UpdateProfileRepository
import com.mevron.rides.driver.updateprofile.domain.repository.IUpdateProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object UpdateProfileModule {

    @Provides
    @Singleton
    fun provideUpdateProfileApi(@Named("mevronCalls") retrofit: Retrofit): UpdateProfileApi =
        retrofit.create(UpdateProfileApi::class.java)

    @Provides
    @Singleton
    fun provideUpdateProfileRepository(api: UpdateProfileApi): IUpdateProfileRepository =
        UpdateProfileRepository(api)
}