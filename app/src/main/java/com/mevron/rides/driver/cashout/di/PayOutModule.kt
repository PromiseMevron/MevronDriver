package com.mevron.rides.driver.cashout.di

import com.mevron.rides.driver.cashout.data.network.PaymentAPI
import com.mevron.rides.driver.cashout.data.repository.PayOutRepository
import com.mevron.rides.driver.cashout.domain.repository.IPayOutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PayOutModule {

    @Provides
    @Singleton
    fun providePayOutApi(retrofit: Retrofit): PaymentAPI =
        retrofit.create(PaymentAPI::class.java)

    @Provides
    @Singleton
    fun providePayOutRepository(api: PaymentAPI): IPayOutRepository =
        PayOutRepository(api)
}