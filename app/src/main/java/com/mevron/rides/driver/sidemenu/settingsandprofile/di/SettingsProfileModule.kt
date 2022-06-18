package com.mevron.rides.driver.sidemenu.settingsandprofile.di

import com.mevron.rides.driver.sidemenu.settingsandprofile.data.network.SettingProfileAPI
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.repository.SettingProfileRepository
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.repository.ISettingProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsProfileModule {

    @Provides
    @Singleton
    fun provideSettingProfileApi(retrofit: Retrofit): SettingProfileAPI =
        retrofit.create(SettingProfileAPI::class.java)

    @Provides
    @Singleton
    fun provideEmergencyRepository(api: SettingProfileAPI): ISettingProfileRepository =
        SettingProfileRepository(api)
}