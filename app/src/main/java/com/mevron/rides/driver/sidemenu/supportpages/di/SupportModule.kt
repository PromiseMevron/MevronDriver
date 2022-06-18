package com.mevron.rides.driver.sidemenu.supportpages.di

import com.mevron.rides.driver.sidemenu.supportpages.data.network.SupportAPI
import com.mevron.rides.driver.sidemenu.supportpages.data.repository.SupportRepository
import com.mevron.rides.driver.sidemenu.supportpages.domain.repository.ISupportRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupportModule {

    @Provides
    @Singleton
    fun provideSaveAddressApi(retrofit: Retrofit): SupportAPI =
        retrofit.create(SupportAPI::class.java)

    @Provides
    @Singleton
    fun provideSaveAddressRepository(api: SupportAPI): ISupportRepository =
        SupportRepository(api)
}