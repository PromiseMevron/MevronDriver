package com.mevron.rides.driver.home.domain

import com.mevron.rides.driver.domain.DomainModel

interface IStateMachineRepository {
    suspend fun getStateMachineState(): DomainModel
}