package com.mevron.rides.driver.domain

interface ISocketManager {

    val isConnected: Boolean

    fun connect()

    fun<T> emitEvent(event: SocketEvent, data: T)

    fun disconnect()
}

sealed interface SocketEvent {
    // different types of event can be emitted with associated data.
}