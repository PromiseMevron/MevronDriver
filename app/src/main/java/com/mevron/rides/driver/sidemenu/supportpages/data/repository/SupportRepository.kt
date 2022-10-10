package com.mevron.rides.driver.sidemenu.supportpages.data.repository

import com.mevron.rides.driver.domain.DomainModel
import com.mevron.rides.driver.remote.HTTPErrorHandler
import com.mevron.rides.driver.sidemenu.supportpages.data.model.NotificationResponse
import com.mevron.rides.driver.sidemenu.supportpages.data.network.SupportAPI
import com.mevron.rides.driver.sidemenu.supportpages.domain.model.SupportDomainData
import com.mevron.rides.driver.sidemenu.supportpages.domain.model.Supports
import com.mevron.rides.driver.sidemenu.supportpages.domain.repository.ISupportRepository
import com.mevron.rides.driver.util.Constants

class SupportRepository (private val api: SupportAPI): ISupportRepository {

    override suspend fun getNotifications() = api.getNotifications().let {
        if (it.isSuccessful) {
            it.body()?.toDomainModel() ?: DomainModel.Error(Throwable(Constants.UNEXPECTED_ERROR))
        } else {
            val error = HTTPErrorHandler.handleErrorWithCode(it)
            DomainModel.Error(Throwable(error?.error?.message ?: Constants.UNEXPECTED_ERROR))
        }
    }

    private fun NotificationResponse.toDomainModel() = DomainModel.Success(
        data = SupportDomainData(
            notifications = this.success.notificationData.result.map {
                Supports(heading = it.title, subHeading = it.description)
            }
        )
    )
}