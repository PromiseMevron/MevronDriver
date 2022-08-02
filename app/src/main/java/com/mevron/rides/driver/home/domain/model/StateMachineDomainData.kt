package com.mevron.rides.driver.home.domain.model

data class StateMachineDomainData(val state: Pair<String, StateMachineMetaData?>)

fun StateMachineDomainData.isEmpty() = state.first.isEmpty()

data class StateMachineMetaData(
    val tripId: String?,
    val status: String?,
    val pickupLatitude: String?,
    val pickupLongitude: String?,
    val destinationLatitude: String?,
    val destinationLongitude: String?,
    val estimatedDistance: Any?,
    val estimatedTripTime: String?,
    val riderRating: String?,
    val riderImage: String?,
    val riderName: String?,
    val destinationAddress: String?,
    val pickupAddress: String?



) {
    companion object {
        val EMPTY = StateMachineMetaData("", "", "", "", "", "", "", "", "", "", "", "", "")
    }
}