package com.mevron.rides.driver.service

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.domain.SocketEvent
import io.socket.client.IO
import java.net.URISyntaxException

private const val SOCKET_URL =
    "http://staging.mevron.com:8083/?uuid=87b86a05-45cb-40de-a1bf-92fd83625888&lat=6.6000652&long=3.2390875"

class SocketManager(private val preferenceRepository: IPreferenceRepository) : ISocketManager {

    private lateinit var socket: io.socket.client.Socket

    override val isConnected: Boolean
        get() = socket.isActive

    override fun connect() {
        try {
            socket = IO.socket(SOCKET_URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

        // manage events.

        socket.open()
    }

    override fun disconnect() {
        if (socket.isActive) {
            socket.disconnect()
        }
    }

    override fun <T> emitEvent(event: SocketEvent, data: T) {
        // process events from outside
        if (!socket.isActive) {
            // enqueue event and connect to be dequeued on connection
            connect()
        } else {
            // emit the event from the socket.
        }
    }
}