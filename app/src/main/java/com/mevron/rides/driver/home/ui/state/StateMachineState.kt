package com.mevron.rides.driver.home.ui.state

import com.mevron.rides.driver.home.domain.model.StateMachineDomainData
import com.mevron.rides.driver.home.domain.model.StateMachineMetaData

data class StateMachineState(
    val isLoading: Boolean,
    val data: StateMachineDomainData,
    val error: String
) {
    companion object {
        val EMPTY = StateMachineState(
            isLoading = false,
            data = StateMachineDomainData(Pair("", StateMachineMetaData.EMPTY)),
            error = ""
        )
    }
}