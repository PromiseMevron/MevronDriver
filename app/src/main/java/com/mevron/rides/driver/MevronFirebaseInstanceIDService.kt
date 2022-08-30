package com.mevron.rides.driver

import com.google.firebase.messaging.FirebaseMessagingService

class MevronFirebaseInstanceIDService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}