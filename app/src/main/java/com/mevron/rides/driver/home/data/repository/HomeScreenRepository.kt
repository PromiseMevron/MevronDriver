package com.mevron.rides.driver.home.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.home.data.model.HomeScreenDataResponse
import com.mevron.rides.driver.home.data.network.HomeScreenApi
import com.mevron.rides.driver.home.domain.IHomeScreenRepository
import com.mevron.rides.driver.home.domain.model.HomeScreenDomainModel

class HomeScreenRepository(private val api: HomeScreenApi) : IHomeScreenRepository {

    override suspend fun toggleStatus(): DomainModel =
        api.toggleStatus().let {
            if (it.isSuccessful) {
                DomainModel.Success(data = Unit)
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }

    override suspend fun getDocumentStatus(): DomainModel =
        api.getHomeStatus().let {
            if (it.isSuccessful) {
                DomainModel.Success(
                    data = it.body()?.toDomainModel()
                        ?: DomainModel.Error(Throwable("No data found"))
                )
            } else {
                DomainModel.Error(Throwable(it.errorBody().toString()))
            }
        }
}

private fun HomeScreenDataResponse.toDomainModel() = this.successData.contentData.let {
    HomeScreenDomainModel(
        documentStatus = it.documentStatus,
        earnings = it.earnings,
        onlineStatus = it.onlineStatus,
        rides = it.rides,
        scheduledPickUps = it.scheduledPickUps,
        weeklyChallenges = it.weeklyChallenges
    )
}