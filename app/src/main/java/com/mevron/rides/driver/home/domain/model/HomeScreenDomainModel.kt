package com.mevron.rides.driver.home.domain.model

data class HomeScreenDomainModel(
    val documentStatus: Int,
    val earnings: Int,
    val onlineStatus: Boolean,
    val rides: Int,
    val scheduledPickUps: List<Any>,
    val weeklyChallenges: List<Any>
)