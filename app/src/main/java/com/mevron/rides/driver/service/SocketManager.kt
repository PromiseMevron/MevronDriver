package com.mevron.rides.driver.service

import android.util.Log
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.domain.SocketEvent
import com.mevron.rides.driver.home.data.model.MetaData
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.home.domain.model.*
import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.ui.ApproachingPassengerData
import com.mevron.rides.driver.home.ui.GoingToDestinationData
import com.mevron.rides.driver.home.ui.StartRideData
import io.socket.client.IO
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.*
import javax.inject.Inject

private const val TAG = "_SocketManager"
private const val SOCKET_URL = "http://staging.mevron.com:8086/"

// Create SocketDataRepository
// This repo will be able to accept different socket data and expose as flow
/**
 * class SocketDataRepository {
 *  private val rideRequestedFlow = MutableFlow<RideRequestDataFromMrBab>()
 *
 *  val rideRequest: StateFlow<RideRequestDataFromMrBab>
 *      get() = rideRequestedFlow
 *
 *  fun setRideReqeusted(rideRequestData: RideReqeustDataFromMrBab) {
 *      rideRequestedFlow.value = rideRequestData
 *  }
 * }
 */
class SocketManager @Inject constructor(private val mapStateRepository: IMapStateRepository) :
    ISocketManager {

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
        // manage events.
        socket?.let { socketInstance ->
            Log.d(TAG, "Sending Location $4444")
            if (!socketInstance.isActive) {
                while (eventQueue.isNotEmpty()) {
                    val currentEvent = eventQueue.poll()
                    currentEvent?.let {
                        socketInstance.emit(it.name, it.toJsonString())
                    }
                }
                //IncomingRideCancelledEvent
                /**
                 *   val passengerImage: String,
                val tripInfo: String,
                val rideDuration: String,
                val distanceRemaining: String
                 */
                socketInstance.on(SocketEvent.Connected.name) {
                    socketInstance.emit("{\"uuid\":\"7f2ca768-3331-4009-a24d-1cb013366f81\",\"type\":\"driver\"}")
                    Log.d(TAG, "Sending Location $2222")
                    if (it.isNotEmpty()) {
                        Log.d(TAG, "Sending Location $333")
                    }
                }

                socketInstance.on(SocketEvent.EventManager.name) {
                    print("the response from event")
                    //SocketEventSuccess
                    Log.d(TAG, "Sending Location $it")
                    if (it.isNotEmpty()){
                        val item = it[0] as JSONObject
                        val data = SocketEvent.EventManager.fromJson(item.toString())
                        Log.d(TAG, "Sending Location 22 $data")
                    }

                    socketInstance.emit("{\"uuid\":\"7f2ca768-3331-4009-a24d-1cb013366f81\",\"type\":\"driver\"}")
                }

                socketInstance.on(SocketEvent.IncomingRideRequestEvent.name) {
                    if (it.isNotEmpty()) {
                        val item = it[0] as JSONObject
                        val data = SocketEvent.IncomingRideRequestEvent.fromJson(item.toString())
                        mapStateRepository.setCurrentState(
                            MapTripState.AcceptRideState(
                                data = AcceptRideData(
                                    passengerImage = "",
                                    tripInfo = "",
                                    rideDuration = data?.rideRequestMetaData?.estimatedTripTime
                                        ?: "",
                                    distanceRemaining = data?.rideRequestMetaData?.estimatedDistance
                                        ?: ""
                                )
                            )
                        )
                    }
                }

                socketInstance.on(SocketEvent.IncomingRideCancelledEvent.name) {
                    if (it.isNotEmpty()) {
                        val item = it[0] as JSONObject
                        SocketEvent.IncomingRideCancelledEvent.fromJson(item.toString())
                        mapStateRepository.setCurrentState(MapTripState.Idle)
                        // process this event.
                        // repo sets incoming request data
                    }
                }

                socketInstance.on(SocketEvent.StateManagerEvent.name) {
                    if (it.isNotEmpty()) {
                        val item = it[0] as JSONObject
                        val data = SocketEvent.StateManagerEvent.fromJson(item.toString())
                        val stateMachineDomainData = data?.toDomainData()
                        when (stateMachineDomainData?.state?.first?.let { it1 ->
                            StateMachineCurrentState.from(
                                it1
                            )
                        }) {
                            StateMachineCurrentState.ORDER -> {
                                mapStateRepository.setCurrentState(MapTripState.Idle)
                            }
                            StateMachineCurrentState.IN_TRIP -> {
                                mapStateRepository.setCurrentState(
                                    inTripRouting(
                                        stateMachineDomainData = stateMachineDomainData
                                    )
                                )
                            }
                            StateMachineCurrentState.PAYMENT -> {

                                mapStateRepository.setCurrentState(MapTripState.Payment)
                            }
                            else -> {}
                        }
                        // process this event.
                        // repo sets incoming request data
                    }
                }
            //    socketInstance.open()
            }
        }
        socket?.open()

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

    private fun StateMachineResponse.toDomainData(): StateMachineDomainData =
        StateMachineDomainData(Pair(currentState, metaData.toDomainModel()))


    private fun MetaData.toDomainModel() = StateMachineMetaData(
        tripId,
        status,
        pickupLatitude,
        pickupLongitude,
        destinationLatitude,
        destinationLongitude,
        estimatedDistance,
        estimatedTripTime,
        riderRating,
        riderImage,
        riderName,
        destinationAddress,
        pickupAddress
    )

    private fun inTripRouting(stateMachineDomainData: StateMachineDomainData): MapTripState {
        val status = InTripStateMachineCurrentState.from(stateMachineDomainData.state.second.status)
        val data = stateMachineDomainData.state.second
        return when (status.state) {
            InTripState.DRIVER_ARRIVED -> {
                val startRide = StartRideData(
                    timeRemainingForPassenger = "",
                    passengerInfo = "Picking up " + data.riderName,
                    timeLeftToPickPassengerInfo = "",
                    passengerDroppedErrorLabel = ""
                )
                MapTripState.StartRideState(startRide)
            }
            InTripState.APPROACHING_PASSENGER -> {
                val approachingPassengerData = ApproachingPassengerData(
                    passengerImage = data.riderImage ?: "",
                    passengerName = data.riderName ?: "",
                    passengerRating = data.riderRating ?: "",
                    timeLeftToPassengerInfo = "",
                    pickUpPassengerInfo = "Picking up " + data.riderName,
                    dropOffAtInfo = "",
                    pickUpLocationInfo = ""
                )

                (MapTripState.ApproachingPassengerState(data = approachingPassengerData))
            }
            InTripState.GOING_TO_DESTINATION -> {
                val goingToDestinationData = GoingToDestinationData(
                    timeToDestinationMessage = "",
                    actionText = "Dropping off " + data.riderName
                )
                (MapTripState.GoingToDestinationState(data = goingToDestinationData))
            }
            else -> {
                MapTripState.Idle
            }
        }
    }
}