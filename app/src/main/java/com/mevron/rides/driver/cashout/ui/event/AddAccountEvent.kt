package com.mevron.rides.driver.cashout.ui.event

sealed interface AddAccountEvent{
    object OnContinueClick : AddAccountEvent
    object OnGetSpecicif : AddAccountEvent
}