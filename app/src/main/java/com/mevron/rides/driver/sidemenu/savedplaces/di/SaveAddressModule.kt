package com.mevron.rides.driver.sidemenu.savedplaces.di

import com.mevron.rides.driver.sidemenu.savedplaces.data.network.AddressAPI
import com.mevron.rides.driver.sidemenu.savedplaces.data.repository.AddressRepository
import com.mevron.rides.driver.sidemenu.savedplaces.domain.repository.IAddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SaveAddressModule {

    @Provides
    @Singleton
    fun provideSaveAddressApi(@Named("mevronCalls") retrofit: Retrofit): AddressAPI =
        retrofit.create(AddressAPI::class.java)

    @Provides
    @Singleton
    fun provideSaveAddressRepository(api: AddressAPI): IAddressRepository =
        AddressRepository(api)
}