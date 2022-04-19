package com.mevron.rides.driver.authentication.ui.createaccount.event

sealed interface CreateAccountEvent {
    object OnCreateAccountClick : CreateAccountEvent
}