package com.mevron.rides.driver.sidemenu.emerg.di

import com.mevron.rides.driver.sidemenu.emerg.data.network.EmergencyAPI
import com.mevron.rides.driver.sidemenu.emerg.data.repository.EmergencyRepository
import com.mevron.rides.driver.sidemenu.emerg.domain.repository.IEmergencyRepository
import com.mevron.rides.driver.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmergencyModule {

    @Provides
    @Singleton
    fun provideEmergencyApi(@Named(Constants.MEVRON_CALL) retrofit: Retrofit): EmergencyAPI =
        retrofit.create(EmergencyAPI::class.java)

    @Provides
    @Singleton
    fun provideEmergencyRepository(api: EmergencyAPI): IEmergencyRepository =
        EmergencyRepository(api)
}