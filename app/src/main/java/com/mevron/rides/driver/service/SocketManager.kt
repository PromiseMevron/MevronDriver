package com.mevron.rides.driver.service

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.domain.SocketEvent
import io.socket.client.IO
import java.net.URISyntaxException

private const val SOCKET_URL =
    "http://staging.mevron.com:8083/?uuid=87b86a05-45cb-40de-a1bf-92fd83625888&lat=6.6000652&long=3.2390875"

class SocketManager(private val preferenceRepository: IPreferenceRepository) : ISocketManager {

    private var socket: io.socket.client.Socket? = null

    override val isConnected: Boolean
        get() = socket?.isActive ?: false

    override fun connect() {
        try {
            if (socket == null) {
                socket = IO.socket(SOCKET_URL)
            }
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

        // manage events.
        socket?.let { socketInstance ->
            if (!socketInstance.isActive) {
                socketInstance.open()
            }
        }
    }

    override fun disconnect() {
        val currentSocket = socket
        if (currentSocket != null) {
            if (currentSocket.isActive) {
                currentSocket.disconnect()
                socket = null
            }
        }
    }

    override fun <T> emitEvent(event: SocketEvent, data: T) {
        // process events from outside
        val currentSocket = socket ?: return
        if (!currentSocket.isActive) {
            // enqueue event and connect to be dequeued on connection
            connect()
        } else {
            // emit the event from the socket.
        }
    }
}