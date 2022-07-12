package com.mevron.rides.driver.sidemenu.vehicle.di

import com.mevron.rides.driver.sidemenu.vehicle.data.network.VehicleApi
import com.mevron.rides.driver.sidemenu.vehicle.data.repository.VehicleRepository
import com.mevron.rides.driver.sidemenu.vehicle.domain.repository.IVehicleRepository
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
object VehicleModule {

    @Provides
    @Singleton
    fun provideVehicleApi(@Named(Constants.MEVRON_CALL) retrofit: Retrofit): VehicleApi =
        retrofit.create(VehicleApi::class.java)

    @Provides
    @Singleton
    fun provideVehicleRepository(api: VehicleApi): IVehicleRepository =
        VehicleRepository(api)
}