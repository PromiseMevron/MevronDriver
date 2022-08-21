package com.mevron.rides.driver.service

import android.util.Log
import com.mevron.rides.driver.authentication.domain.repository.IPreferenceRepository
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.domain.SocketEvent
import com.mevron.rides.driver.domain.SocketName.CONNECTED
import com.mevron.rides.driver.home.data.model.MetaData
import com.mevron.rides.driver.home.data.model.StateMachineResponse
import com.mevron.rides.driver.home.domain.IMapStateRepository
import com.mevron.rides.driver.home.domain.model.*
import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.ui.ApproachingPassengerData
import com.mevron.rides.driver.home.ui.GoingToDestinationData
import com.mevron.rides.driver.home.ui.PayData
import com.mevron.rides.driver.home.ui.StartRideData
import com.mevron.rides.driver.util.Constants
import io.socket.client.IO
import io.socket.client.Socket
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
class SocketManager @Inject constructor(
    private val mapStateRepository: IMapStateRepository,
    private val prefrenceRepository: IPreferenceRepository
) :
    ISocketManager {

    fun <T : Any, U : Any> toData(keys: Array<T>, values: Array<U>): Map<T, U> {
        val map = mutableMapOf<T, U>()

        for (index in keys.indices) {
            map[keys[index]] = values[index]
        }

        return map.toMap()
    }

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
                    //  socketInstance.emit("{\"uuid\":\"4b6c74e8-f135-426d-bd06-4e4dca70eae4\",\"type\":\"driver\"}")
                    Log.d(TAG, "Sending Location $2222")

                    if (it.isNotEmpty()) {
                        val item = it[0] as JSONObject
                        val data = SocketEvent.Connected.fromJson(item.toString())
                        Log.d(TAG, "Sending Location 33 $data")
                        // socketInstance.emit(CONNECTED,"{\"uuid\":\"4b6c74e8-f135-426d-bd06-4e4dca70eae4\",\"type\":\"driver\"}")
                    }

                    val uuid = prefrenceRepository.getStringForKey(Constants.UUID)
                    //  socketInstance.emit(CONNECTED, SocketEvent.Connected.toData(arrayOf("uuid", "type"), arrayOf(uuid, "driver")), {
                    //     Log.d(TAG, "Sending Location 45554 ")
                    // })
                }

                socketInstance.on(SocketEvent.EventManager.name) {
                    print("the response from event")
                    //SocketEventSuccess
                    Log.d(TAG, "Sending Location 87878 $it")
                    if (it.isNotEmpty()) {
                       // val item = it[0] as JSONObject
                        //val data = SocketEvent.EventManager.fromJson(item.toString())
                       // Log.d(TAG, "Sending Location 22 $data")
                        val uuid = prefrenceRepository.getStringForKey(Constants.UUID)
                        socketInstance.emit(CONNECTED, "{\"uuid\":\"${uuid}\",\"type\":\"driver\"}")
                        incomingRideRequestEvent(socketInstance)
                        cancelledEvent(socketInstance)
                        stateManagerEvent(socketInstance)
                    }
                    //
                    //  socketInstance.emit(EVENT, SocketEvent.EventManager.toData(arrayOf("uuid", "type"), arrayOf(uuid, "driver")))
                }
                socketInstance.open()
            }
        }
        socket?.open()

    }

    private fun stateManagerEvent(socketInstance: Socket) {
       // Log.d("State Machine", "State Machine 1")
        socketInstance.on(SocketEvent.StateManagerEvent.name) {
            if (it.isNotEmpty()) {
             //   Log.d("State Machine", "State Machine 2 $it")
                val item = it[0] as JSONObject
                val data = SocketEvent.StateManagerEvent.fromJson(item.toString())
                Log.d("State Machine", "State Machine 3  $data")
                val stateMachineDomainData = data?.toDomainData()
             //   Log.d("State Machine", "State Machine  $stateMachineDomainData")
                when (stateMachineDomainData?.state?.first?.let { it1 ->
                    StateMachineCurrentState.from(
                        it1
                    )
                }) {
                    StateMachineCurrentState.ORDER -> {

                        mapStateRepository.setCurrentState(
                            MapTripState.AcceptRideState(
                                data = AcceptRideData(
                                    passengerImage = data.metaData?.riderImage ?: "",
                                    tripInfo = "Pick up is ${data.metaData?.estimatedDistance} away",
                                    rideDuration = data.metaData?.estimatedTripTime
                                        ?: "",
                                    distanceRemaining = data.metaData?.estimatedDistance.toString()
                                        ?: ""
                                )
                            ).also { state ->
                                state.tripId = data.metaData?.tripId
                                prefrenceRepository.setStringForKey("TRIPID", data.metaData?.tripId ?: "")
                            //    state.tripId = data.metaData?.tripId
                            }
                        )
                    }

                    StateMachineCurrentState.IDLE -> {
                     //   mapStateRepository.setCurrentState(MapTripState.Idle)
                    }
                    StateMachineCurrentState.IN_TRIP -> {
                        mapStateRepository.setCurrentState(
                            inTripRouting(
                                stateMachineDomainData = stateMachineDomainData
                            ).also { state ->
                              //  state.tripId = data.metaData?.tripId
                            }
                        )
                    }
                    StateMachineCurrentState.PAYMENT -> {
                        Log.d("State Machine", "State Machine  we are enetring here")
                        mapStateRepository.setCurrentState(
                            MapTripState.Payment(
                                PayData(
                                    image = data.metaData?.riderImage ?: "",
                                    amount = data.metaData?.amount ?: "0",
                                    name = data.metaData?.riderName ?: "",
                                    currency = data.metaData?.currency ?: ""
                                )
                            ).also { state ->
                              //  state.tripId = data.metaData?.tripId
                            }
                        )
                    }
                    StateMachineCurrentState.RATING -> {
                        Log.d("State Machine", "State Machine  we are enetring here not")
                        mapStateRepository.setCurrentState(
                            MapTripState.Rating(
                                PayData(
                                    image = data.metaData?.riderImage ?: "",
                                    amount = data.metaData?.amount ?: "0",
                                    name = data.metaData?.riderName ?: "",
                                    currency = data.metaData?.currency ?: ""
                                )
                            ).also { state ->
                             //   state.tripId = data.metaData?.tripId
                            }
                        )
                    }
                    else -> {}
                }
                // process this event.
                // repo sets incoming request data
            }
        }
    }

    private fun cancelledEvent(socketInstance: Socket) {
        socketInstance.on(SocketEvent.IncomingRideCancelledEvent.name) {
            if (it.isNotEmpty()) {
                val item = it[0] as JSONObject
                SocketEvent.IncomingRideCancelledEvent.fromJson(item.toString())
                mapStateRepository.setCurrentState(MapTripState.Idle)
                // process this event.
                // repo sets incoming request data
            }
        }
    }

    private fun incomingRideRequestEvent(socketInstance: Socket) {
        socketInstance.on(SocketEvent.IncomingRideRequestEvent.name) {
            Log.d(TAG, "Sending Location 090909 $it")
            if (it.isNotEmpty()) {
                val item = it[0] as JSONObject
                val data = SocketEvent.IncomingRideRequestEvent.fromJson(item.toString())
                prefrenceRepository.setStringForKey(
                    Constants.TRIP_ID,
                    data?.rideRequestMetaData?.tripId ?: ""
                )
                Log.d(TAG, "Sending Location 098787 $data")
                mapStateRepository.setCurrentState(
                    MapTripState.AcceptRideState(
                        data = AcceptRideData(
                            passengerImage = data?.rideRequestMetaData?.riderImage ?: "",
                            tripInfo = "Pick up is ${data?.rideRequestMetaData?.estimatedPickupTime} away",
                            rideDuration = data?.rideRequestMetaData?.estimatedTripTime
                                ?: "",
                            distanceRemaining = data?.rideRequestMetaData?.estimatedDistance
                                ?: ""
                        )
                    ).also { state ->
                        state.tripId = data?.rideRequestMetaData?.tripId
                        prefrenceRepository.setStringForKey("TRIPID", data?.rideRequestMetaData?.tripId ?: "")
                    }
                )
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

    private fun StateMachineResponse.toDomainData(): StateMachineDomainData =
        StateMachineDomainData(Pair(currentState, metaData?.toDomainModel()))


    private fun MetaData.toDomainModel() = StateMachineMetaData(
        tripId = tripId ?: "",
        status = status ?: "",
        pickupLatitude = pickupLatitude ?: "0.0",
        pickupLongitude ?: "0.0",
        destinationLatitude ?: "0.0",
        destinationLongitude ?: "0.0",
        estimatedDistance ?: "",
        estimatedTripTime ?: "",
        riderRating ?: "",
        riderImage ?: "",
        riderName ?: "",
        destinationAddress ?: "",
        pickupAddress ?: "",
        amount ?: "", currency ?: ""
    )

    private fun inTripRouting(stateMachineDomainData: StateMachineDomainData): MapTripState {
        val status =
            InTripStateMachineCurrentState.from(stateMachineDomainData.state.second?.status)
        val data = stateMachineDomainData.state.second
        return when (status.state) {
            InTripState.DRIVER_ARRIVED -> {
                val startRide = StartRideData(
                    timeRemainingForPassenger = "",
                    passengerInfo = "Picking up " + data?.riderName,
                    timeLeftToPickPassengerInfo = "",
                    passengerDroppedErrorLabel = ""
                )
                MapTripState.StartRideState(startRide)
            }
            InTripState.APPROACHING_PASSENGER -> {
                val approachingPassengerData = ApproachingPassengerData(
                    passengerImage = data?.riderImage ?: "",
                    passengerName = data?.riderName ?: "",
                    passengerRating = data?.riderRating ?: "",
                    timeLeftToPassengerInfo = "",
                    pickUpPassengerInfo = "Picking up " + data?.riderName,
                    dropOffAtInfo = "",
                    pickUpLocationInfo = ""
                )

                (MapTripState.ApproachingPassengerState(data = approachingPassengerData))
            }
            InTripState.GOING_TO_DESTINATION -> {
                val goingToDestinationData = GoingToDestinationData(
                    timeToDestinationMessage = "",
                    actionText = "Dropping off " + data?.riderName
                )
                (MapTripState.GoingToDestinationState(data = goingToDestinationData))
            }
            else -> {
                MapTripState.Idle
            }
        }
    }
}