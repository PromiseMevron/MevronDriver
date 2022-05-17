package com.mevron.rides.driver.shared

fun <T> T?.whenNotNull(consumer: (T) -> Unit) {
    if (this == null) return
    consumer(this)
}