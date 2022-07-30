package com.mevron.rides.driver.domain

import com.google.gson.GsonBuilder
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.data.model.sockets.RideRequestSocketData
import com.mevron.rides.driver.location.domain.model.LocationData
import com.mevron.rides.driver.service.SocketEventSuccess

// TODO REMOVE as soon as possible
private const val TEST_UUID = "87b86a05-45cb-40de-a1bf-92fd83625888"

interface ISocketManager {

    val isConnected: Boolean

    fun connect()

    fun emitEvent(event: SocketEvent)

    fun disconnect()
}

sealed interface SocketEvent {

    fun <T : Any, U : Any> toData(keys: Array<T>, values: Array<U>): Map<T, U> {
        val map = mutableMapOf<T, U>()

        for (index in keys.indices) {
            map[keys[index]] = values[index]
        }

        return map.toMap()
    }

    fun fromJson(string: String): Any?
    fun toJsonString(): String
    val name: String

    object Connected : SocketEvent {

        override fun fromJson(string: String): Any? {
           return null
        }

        override fun toJsonString(): String {
            return "Driver connected"
        }

        override val name: String
            get() = "connected"
    }

    data class SendLocationEvent(val locationData: LocationData) : SocketEvent {

        override fun fromJson(string: String): LocationData? {
            val gson = GsonBuilder().setPrettyPrinting().create()
            return gson.fromJson(string, LocationData::class.java)
        }

        override fun toJsonString(): String {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val map = hashMapOf<String, String>()
            map["lat"] = "${locationData.lat}"
            map["long"] = "${locationData.long}"
            map["direction"] = "${locationData.direction}"
            map["uuid"] = TEST_UUID
            return gson.toJson(map)
        }

        override val name: String = SocketName.SEND_DRIVER_LOCATION
    }

    object IncomingRideRequestEvent : IncomingEvent() {

        override fun fromJson(string: String): RideRequestSocketData? {
            val gson = GsonBuilder().setPrettyPrinting().create()
            // TODO map what mr babafemi sends for incoming socket event
            return gson.fromJson(string, RideRequestSocketData::class.java)
        }

        override val name: String = SocketName.RIDE_REQUESTED
    }

    object IncomingRideCancelledEvent : IncomingEvent() {

        override fun fromJson(string: String): LocationData? {
            val gson = GsonBuilder().setPrettyPrinting().create()
            // TODO map what mr babafemi sends for incoming socket event
            return gson.fromJson(string, LocationData::class.java)
        }

        override val name: String = SocketName.RIDER_CANCELLED
    }

    object StateManagerEvent : IncomingEvent() {

        override fun fromJson(string: String): StateMachineResponse? {
            val gson = GsonBuilder().setPrettyPrinting().create()
            // TODO map what mr babafemi sends for incoming socket event
            return gson.fromJson(string, StateMachineResponse::class.java)
        }

        override val name: String = SocketName.STATE_MANAGER
    }

    object EventManager: SocketEvent{
        override fun fromJson(string: String): SocketEventSuccess? {
            print("we received a response")
            val gson = GsonBuilder().setPrettyPrinting().create()
            return gson.fromJson(string, SocketEventSuccess::class.java)
           // return null
        }

        override fun toJsonString(): String {
            return ""
        }

        override val name: String
            get() = SocketName.EVENT

    }
}

abstract class IncomingEvent : SocketEvent {
    override fun toJsonString(): String = ""
}

object SocketName {
    const val NEAR_BY = "nearby_drivers"
    const val SEARCH_DRIVERS = "search_drivers"
    const val TRIP_STATUS = "trip_status"
    const val DRIVER_PICK_UP = "trip_status"
    const val EVENT = "event"
    const val CONNECTED = "connected"
    const val STATE_MANAGER = "driver_state_machine"
    const val RIDE_REQUESTED = "ride_requests"
    const val SEND_DRIVER_LOCATION = "driver_location"
    const val RIDER_CANCELLED = "rider_cancelled"
}