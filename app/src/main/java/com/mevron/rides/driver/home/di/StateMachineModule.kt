package com.mevron.rides.driver.home.di

import com.mevron.rides.driver.home.data.network.StateMachineApi
import com.mevron.rides.driver.home.data.repository.StateMachineRepository
import com.mevron.rides.driver.home.domain.IStateMachineRepository
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
object StateMachineModule {

    @Provides
    @Singleton
    fun provideStateMachineApi(@Named(MEVRON_CALL) retrofit: Retrofit): StateMachineApi =
        retrofit.create(StateMachineApi::class.java)

    @Provides
    @Singleton
    fun provideStateMachineRepository(
        api: StateMachineApi
    ): IStateMachineRepository = StateMachineRepository(api)
}