package com.mevron.rides.driver.sidemenu.supportpages.domain.repository

import com.mevron.rides.driver.domain.DomainModel

interface ISupportRepository {
    suspend fun getNotifications(): DomainModel
}