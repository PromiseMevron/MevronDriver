package com.mevron.rides.driver.updateprofile.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.updateprofile.data.model.CarMakesResponse
import com.mevron.rides.driver.updateprofile.data.model.CarModelResponse
import com.mevron.rides.driver.updateprofile.data.model.CarYearResponse
import com.mevron.rides.driver.updateprofile.data.network.CarPropertiesApi
import com.mevron.rides.driver.updateprofile.domain.model.CarMake
import com.mevron.rides.driver.updateprofile.domain.model.CarMakesDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarModel
import com.mevron.rides.driver.updateprofile.domain.model.CarModelDomainData
import com.mevron.rides.driver.updateprofile.domain.model.CarYearData
import com.mevron.rides.driver.updateprofile.domain.model.CarYearDomainData
import com.mevron.rides.driver.updateprofile.domain.repository.ICarPropertiesRepository
import retrofit2.Response

class CarPropertiesRepository(
    private val api: CarPropertiesApi
) : ICarPropertiesRepository {

    override suspend fun getCarMakes(): DomainModel = responseToDomainModel(api.getCarMakes())

    override suspend fun getCarModels(params: HashMap<String, String>): DomainModel =
        carModelResponseToDomainModel(api.getCarModels(params))

    override suspend fun getCarYears(params: HashMap<String, String>): DomainModel =
        api.getCarYear(params).let { response ->
            if (response.isSuccessful) {
                response.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Request Failed"))
            } else {
                DomainModel.Error(Throwable(response.errorBody().toString()))
            }
        }

    private fun responseToDomainModel(response: Response<CarMakesResponse>) =
        if (response.isSuccessful) {
            response.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Request Failed"))
        } else {
            DomainModel.Error(Throwable(response.errorBody().toString()))
        }

    private fun carModelResponseToDomainModel(response: Response<CarModelResponse>) =
        if (response.isSuccessful) {
            response.body()?.toDomainModel() ?: DomainModel.Error(Throwable("Request Failed"))
        } else {
            DomainModel.Error(Throwable(response.errorBody().toString()))
        }

    private fun CarMakesResponse.toDomainModel() =
        DomainModel.Success(
            data = CarMakesDomainData(makes = this.carMakesSuccessData.carMakesList.map {
                CarMake(it.make)
            })
        )

    private fun CarModelResponse.toDomainModel() =
        DomainModel.Success(
            data = CarModelDomainData(
                carModels = this.carModelSuccessData.carModelList.map {
                    CarModel(
                        model = it.model,
                        id = it.id
                    )
                }
            )
        )

    private fun CarYearResponse.toDomainModel(): DomainModel.Success =
        DomainModel.Success(
            data = CarYearDomainData(
                carYearDataList = this.carYearSuccessData.carYearDataList.map {
                    CarYearData(
                        year = it.year,
                        id = it.id
                    )
                }
            )
        )
}


