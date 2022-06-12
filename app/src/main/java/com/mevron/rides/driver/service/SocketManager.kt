package com.mevron.rides.driver.service

import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.domain.SocketEvent
import io.socket.client.IO
import java.net.URISyntaxException
import java.util.PriorityQueue
import java.util.Queue

private const val TAG = "_SocketManager"
private const val SOCKET_URL = "http://staging.mevron.com:8086/"

class SocketManager(private val preferenceRepository: IPreferenceRepository) : ISocketManager {

    private var socket: io.socket.client.Socket? = null

    private val eventQueue: Queue<SocketEvent> = PriorityQueue()

    override val isConnected: Boolean
        get() = socket?.isActive ?: false

    override fun connect() {
        try {
            socket = IO.socket(SOCKET_URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

        socket?.open()

        // manage events.
        socket?.let { socketInstance ->
            if (!socketInstance.isActive) {
                socketInstance.open()

                while (eventQueue.isNotEmpty()) {
                    val currentEvent = eventQueue.poll()
                    currentEvent?.let {
                        socketInstance.emit(it.name, it.toJsonString())
                    }
                }
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

    override fun emitEvent(event: SocketEvent) {
        // process events from outside
        val currentSocket = socket
        if (currentSocket == null) {
            // enqueue event and connect to be dequeued on connection
            eventQueue.offer(event)
            connect()
        } else {
            if (!currentSocket.isActive) connect()
            currentSocket.emit(event.name, event.toJsonString())
        }
    }
}