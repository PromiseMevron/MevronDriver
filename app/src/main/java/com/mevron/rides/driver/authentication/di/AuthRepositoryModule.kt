package com.mevron.rides.driver.authentication.di

import com.mevron.rides.driver.authentication.data.network.AuthApi
import com.mevron.rides.driver.authentication.data.repository.AuthRepository
import com.mevron.rides.driver.authentication.domain.repository.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthApi(@Named("mevronCalls") retrofit: Retrofit) = retrofit.create<AuthApi>()

    @Provides
    @Singleton
    fun provideAuthRepository(authApi: AuthApi): IAuthRepository = AuthRepository(authApi)
}