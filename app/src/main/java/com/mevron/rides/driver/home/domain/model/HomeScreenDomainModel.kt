package com.mevron.rides.driver.home.domain.model

data class HomeScreenDomainModel(
    val documentStatus: Int,
    val earnings: Int,
    val onlineStatus: Boolean,
    val rides: Int,

    // TODO to be resolved
    val scheduledPickUps: List<Any>, // fixme This is an issue, I don't know what this is
    val weeklyChallenges: List<Any> // fixme this has to be fixed as well
)