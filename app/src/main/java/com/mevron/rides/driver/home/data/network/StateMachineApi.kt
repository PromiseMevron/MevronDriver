package com.mevron.rides.driver.home.data.network

import com.mevron.rides.driver.home.data.model.StateMachineResponse
import retrofit2.Response
import retrofit2.http.GET

interface StateMachineApi {
    @GET("api/v1/driver/auth/state-machine")
    suspend fun getStateMachine(): Response<StateMachineResponse>
}