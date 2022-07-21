package com.mevron.rides.driver.home.domain.model

import com.mevron.rides.driver.home.data.model.home.Drive
import com.mevron.rides.driver.home.data.model.home.Earnings

data class HomeScreenDomainModel(
    val documentStatus: Int,
    val onlineStatus: Boolean,
    val rides: Int,
    val drive: Drive,
    val earnings: Earnings
)