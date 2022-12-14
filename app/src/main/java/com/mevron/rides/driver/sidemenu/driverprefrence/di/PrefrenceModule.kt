package com.mevron.rides.driver.sidemenu.driverprefrence.di

import com.mevron.rides.driver.sidemenu.driverprefrence.data.network.PrefrenceAPI
import com.mevron.rides.driver.sidemenu.driverprefrence.data.repository.PrefrenceRepository
import com.mevron.rides.driver.sidemenu.driverprefrence.domain.repository.IPrefrenceRepository
import com.mevron.rides.driver.util.Constants.MEVRON_CALL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrefrenceModule {

    @Provides
    @Singleton
    fun provideSaveAddressApi(@Named(MEVRON_CALL) retrofit: Retrofit): PrefrenceAPI =
        retrofit.create(PrefrenceAPI::class.java)

    @Provides
    @Singleton
    fun provideSaveAddressRepository(api: PrefrenceAPI): IPrefrenceRepository =
        PrefrenceRepository(api)
}