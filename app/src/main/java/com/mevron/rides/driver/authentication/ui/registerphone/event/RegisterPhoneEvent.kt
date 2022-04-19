package com.mevron.rides.driver.authentication.ui.registerphone.event

sealed interface RegisterPhoneEvent {
    object NextButtonClick : RegisterPhoneEvent
    object DialogOkClick : RegisterPhoneEvent
}