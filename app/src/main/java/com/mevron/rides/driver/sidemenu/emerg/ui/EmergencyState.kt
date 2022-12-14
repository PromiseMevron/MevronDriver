package com.mevron.rides.driver.sidemenu.emerg.ui

import com.mevron.rides.driver.sidemenu.emerg.data.model.Set
import com.mevron.rides.driver.sidemenu.emerg.domain.model.GetContactDomainData

data class EmergencyState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: String,
    val data: MutableList<Set>,
    val openNextPage: Boolean,
    val backButton: Boolean,
    val name: String,
    val phone: String,
    val result: MutableList<GetContactDomainData>,
    val time: MutableList<Int>,
    val contactId: String
) {
    companion object {
        val EMPTY = EmergencyState(
            isLoading = false,
            isSuccess = false,
            error = "",
            data = mutableListOf(),
            openNextPage = false,
            backButton = false,
            name = "",
            phone = "",
            result = mutableListOf(),
            time = mutableListOf(),
            contactId = ""
        )
    }
}
