package com.mevron.rides.driver.domain

import com.google.gson.GsonBuilder
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.data.model.sockets.RideRequestSocketData
import com.mevron.rides.driver.location.domain.model.LocationData

// TODO REMOVE as soon as possible
private const val TEST_UUID = "87b86a05-45cb-40de-a1bf-92fd83625888"

interface ISocketManager {

    val isConnected: Boolean

    fun connect()

    fun emitEvent(event: SocketEvent)

    fun disconnect()
}

sealed interface SocketEvent {

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
            map["lat"] = "${locationData.latitude}"
            map["long"] = "${locationData.longitude}"
            map["direction"] = "${locationData.bearing}"
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
}

abstract class IncomingEvent : SocketEvent {
    override fun toJsonString(): String = ""
}

object SocketName {
    const val NEAR_BY = "nearby_drivers"
    const val SEARCH_DRIVERS = "search_drivers"
    const val TRIP_STATUS = "trip_status"
    const val DRIVER_PICK_UP = "trip_status"
    const val STATE_MANAGER = "state_manager"
    const val RIDE_REQUESTED = "ride_requests"
    const val SEND_DRIVER_LOCATION = "driver_location"
    const val RIDER_CANCELLED = "rider_cancelled"
}