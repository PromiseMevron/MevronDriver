package com.mevron.rides.driver.sidemenu.supportpages.ui.notification.state

import com.mevron.rides.driver.sidemenu.supportpages.domain.model.Supports

data class NotificationState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val error: String,
    val data: MutableList<Supports>,
    val backButton: Boolean
) {
    companion object {
        val EMPTY = NotificationState(
            isLoading = false,
            isSuccess = false,
            error = "",
            data = mutableListOf(),
            backButton = false
        )
    }
}
