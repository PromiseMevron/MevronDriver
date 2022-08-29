package com.mevron.rides.driver.sidemenu.vehicle.data.repository

import com.mevron.rides.driver.auth.model.GeneralResponse
import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.sidemenu.vehicle.data.model.AllVehiclesResponse
import com.mevron.rides.driver.sidemenu.vehicle.data.model.vehicledetail.VehicleDetailResponse
import com.mevron.rides.driver.sidemenu.vehicle.data.network.VehicleApi
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainData
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainDatum
import com.mevron.rides.driver.sidemenu.vehicle.domain.repository.IVehicleRepository

class VehicleRepository(private val api: VehicleApi) : IVehicleRepository {
    override suspend fun getVehicles(): DomainModel = api.getAllVehicles().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("failure to fetch all vehicles"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun getVehicleDetails(id: String): DomainModel = api.getAVehicles(id).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("failure to fetch all vehicles"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }

    override suspend fun deleteVehicle(id: String): DomainModel = api.deleteVehicles(id).let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel()
                ?: DomainModel.Error(Throwable("failure to fetch all vehicles"))
        } else {
            DomainModel.Error(Throwable(it.errorBody().toString()))
        }
    }


    private fun AllVehiclesResponse.toDomainModel() = DomainModel.Success(
        data = AllVehicleDomainData(data = this.allVehicle.allVehicleData.map {
            AllVehicleDomainDatum(
                it.color,
                it.id,
                it.image ?: "",
                it.make,
                it.model,
                it.plateNumber,
                it.uuid,
                it.year
            )
        })
    )

    private fun VehicleDetailResponse.toDomainModel() = DomainModel.Success(
        data = this.vehicleDetailSuccess.vehicleDetailData
    )

    private fun GeneralResponse.toDomainModel() = DomainModel.Success(
        data = this
    )
}