package com.mevron.rides.driver.home.domain.model

data class StateMachineDomainData(val state: Pair<String, StateMachineMetaData>)

fun StateMachineDomainData.isEmpty() = state.first.isEmpty()

data class StateMachineMetaData(
    val tripId: String?,
    val status: String
) {
    companion object {
        val EMPTY = StateMachineMetaData("", "")
    }
}