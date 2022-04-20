package com.mevron.rides.driver.authentication.ui.verifyotp.event

sealed interface VerifyOTPEvent {
    object OnOTPComplete : VerifyOTPEvent
}