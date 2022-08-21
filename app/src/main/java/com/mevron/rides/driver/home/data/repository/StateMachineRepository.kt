package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.MetaData
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.data.network.StateMachineApi
import com.mevron.rides.driver.home.domain.IStateMachineRepository
import com.mevron.rides.driver.home.domain.model.StateMachineDomainData
import com.mevron.rides.driver.home.domain.model.StateMachineMetaData

class StateMachineRepository(private val api: StateMachineApi) : IStateMachineRepository {

    override suspend fun getStateMachineState(): DomainModel {
        val result = api.getStateMachine()
        return if (result.isSuccessful) {
            result.body()?.toDomainData()
                ?: DomainModel.Error(Throwable("Error getting state machine data"))
        } else {
            DomainModel.Error(Throwable(result.errorBody().toString()))
        }
    }

    private fun StateMachineResponse.toDomainData(): DomainModel.Success =
        DomainModel.Success(
            data = StateMachineDomainData(Pair(currentState, metaData?.toDomainModel()))
        )

    private fun MetaData.toDomainModel() = StateMachineMetaData(
        tripId,
        status,
        pickupLatitude,
        pickupLongitude,
        destinationLatitude,
        destinationLongitude,
        estimatedDistance,
        estimatedTripTime,
        riderRating,
        riderImage,
        riderName,
        destinationAddress,
        pickupAddress,
        amount,
        currency
    )
}

