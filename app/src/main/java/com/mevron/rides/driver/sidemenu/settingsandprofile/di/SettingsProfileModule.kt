package com.mevron.rides.driver.sidemenu.settingsandprofile.di

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.network.SettingProfileAPI
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.repository.SettingProfileRepository
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
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
object SettingsProfileModule {

    @Provides
    @Singleton
    fun provideSettingProfileApi(@Named(Constants.MEVRON_CALL) retrofit: Retrofit): SettingProfileAPI =
        retrofit.create(SettingProfileAPI::class.java)

    @Provides
    @Singleton
    fun provideEmergencyRepository(api: SettingProfileAPI): ISettingProfileRepository =
        SettingProfileRepository(api)
}