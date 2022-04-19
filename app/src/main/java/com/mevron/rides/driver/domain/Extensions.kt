package com.mevron.rides.driver.domain

import kotlinx.coroutines.flow.MutableStateFlow

fun<T> MutableStateFlow<T>.update(func: () -> T) {
    this.value = func()
}