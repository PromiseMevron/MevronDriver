package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mevron.rides.driver.R

class ApproachPassengerWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener,
    OnContactClickedListener, OnArriveButtonClickedListener {

 //   private var contactContainer: ConstraintLayout
    private var passengerImage: ImageView
    private var passengerName: TextView
    private var pickUp: TextView
    private var dropOffAtValue: TextView
    private var dropOffBackground: View
    private var navigateToHomeBackground: View
    private var stopNewRideRequestButton: Button
    private var cancelRideRequestButton: Button
    private var passengerRating: TextView
    private var passengerContactWidget: PassengerContactWidget
    private var arrivedAtPassengerWidget: ArrivedAtPickupWidget
    private var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var root: View
    private var bottomSheetRoot: ConstraintLayout
    private var data: ApproachingPassengerData? = null

    private var eventsClickListener: ApproachPassengerWidgetEventClickListener? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_approach_passenger_bottom_sheet, this, true)
        root = findViewById(R.id.bottomSheetLink)

     //   contactContainer = root.findViewById(R.id.contactContainer)
        passengerImage = root.findViewById(R.id.passengerImage)
        passengerName = root.findViewById(R.id.passengerName)
        pickUp = root.findViewById(R.id.pickUpValue)
        dropOffAtValue = root.findViewById(R.id.dropOffAtValue)
        dropOffBackground = root.findViewById(R.id.dropOffBackground)
        navigateToHomeBackground = root.findViewById(R.id.navigateToHomeBackground)
        stopNewRideRequestButton = root.findViewById(R.id.stopNewRideRequestsButton)
        cancelRideRequestButton = root.findViewById(R.id.cancelRideButton)
        passengerRating = root.findViewById(R.id.passengerRating)
        passengerContactWidget = root.findViewById(R.id.goingToPickupWidget)
        arrivedAtPassengerWidget = root.findViewById(R.id.arrivedAtPickupWidget)
        bottomSheetRoot = root.findViewById(R.id.bottomSheetApproachPassenger)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRoot)

        arrivedAtPassengerWidget.setOnContactClickedListener(this)
        passengerContactWidget.setContactClickListener(this)
        arrivedAtPassengerWidget.setOnArriveButtonClickListener(this)
        cancelRideRequestButton.setOnClickListener(this)
        stopNewRideRequestButton.setOnClickListener(this)
        navigateToHomeBackground.setOnClickListener(this)
        dropOffBackground.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cancelRideButton -> eventsClickListener?.onCancelRideClicked()
            R.id.stopNewRideRequestsButton -> eventsClickListener?.onStopNewRideRequestClicked()
            R.id.navigateToHomeBackground -> eventsClickListener?.onNavigateToHomeClicked()
            R.id.dropOffBackground -> eventsClickListener?.onDropOffClicked()
        }
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    private fun setPassengerRating(rating: String) {
        passengerRating.text = rating
    }

    fun showDriverArrivalStatus(hasArrived: Boolean) {
        if (hasArrived) {
            passengerContactWidget.hide()
            arrivedAtPassengerWidget.show()
        } else {
            passengerContactWidget.show()
            arrivedAtPassengerWidget.hide()
        }
    }

    private fun bindArrivedData(arrivedData: ArrivedData) {
        arrivedAtPassengerWidget.bind(arrivedData)
    }

    private fun setDropOffAtValue(dropOffAtValue: String) {
        this.dropOffAtValue.text = dropOffAtValue
    }

    private fun setPassengerName(passengerName: String) {
        this.passengerName.text = passengerName
    }

    override fun onCallClicked() {
        eventsClickListener?.onCallClicked(data?.riderNumber ?: "")
    }

    override fun onMessageClicked() {
        eventsClickListener?.onMessageClicked()
    }

    override fun onArriveClicked() {
        eventsClickListener?.onDriverArrivedClick()
    }

    fun setEventsClickListener(listener: ApproachPassengerWidgetEventClickListener) {
        eventsClickListener = listener
    }

    fun bindData(data: ApproachingPassengerData) {
        this.data = data
        arrivedAtPassengerWidget.bind(
            ArrivedData(
                data.passengerName,
                data.passengerRating,
                data.passengerImage
            )
        )
        passengerContactWidget.bind(
            OnTheWayToPassengerData(
                data.timeLeftToPassengerInfo,
                data.pickUpPassengerInfo
            )
        )

        setDropOffAtValue(data.dropOffAtInfo)
        setPassengerName(data.passengerName)
        bindArrivedData(ArrivedData(data.passengerName, data.passengerRating, data.passengerImage))
        setPassengerRating(data.passengerRating)
    }
}

interface ApproachPassengerWidgetEventClickListener {
    fun onCallClicked(phone: String)
    fun onMessageClicked()
    fun onCancelRideClicked()
    fun onStopNewRideRequestClicked()
    fun onDriverArrivedClick()
    fun onNavigateToHomeClicked()
    fun onDropOffClicked()
}

data class ApproachingPassengerData(
    val passengerImage: String,
    val passengerName: String,
    val passengerRating: String,
    val timeLeftToPassengerInfo: String,
    val pickUpPassengerInfo: String,
    val dropOffAtInfo: String,
    val pickUpLocationInfo: String,
    val riderNumber: String
)