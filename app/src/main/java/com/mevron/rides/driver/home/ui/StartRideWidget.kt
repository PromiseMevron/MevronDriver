package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.ncorti.slidetoact.SlideToActView

class StartRideWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var passengerOut: LayoutError
    private var timeRemainingForPassenger: TextView
    private var startRideButton: SlideToActView
    private var passengerContact: PassengerContactWidget

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_start_the_ride, this, true)
        passengerOut = findViewById(R.id.passengerOut)
        timeRemainingForPassenger = findViewById(R.id.timeRemainingForPassengerArrival)
        startRideButton = findViewById(R.id.startRideButton)
        passengerContact = findViewById(R.id.passengerContact)
    }

    fun setOnContactClickedListener(listener: OnContactClickedListener) {
        passengerContact.setContactClickListener(listener)
    }

    private fun setErrorLabel(label: String) {
        passengerOut.setErrorLabel(label)
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    fun bindData(startRideData: StartRideData) {
        startRideData.passengerDroppedErrorLabel?.let { setErrorLabel(it) }
        passengerContact.bind(
            OnTheWayToPassengerData(
                startRideData.timeRemainingForPassenger,
                startRideData.passengerInfo
            )
        )
        timeRemainingForPassenger.text = startRideData.timeLeftToPickPassengerInfo
    }

    fun setSlideCompleteCallback(listener: SlideToActView.OnSlideCompleteListener) {
        startRideButton.onSlideCompleteListener = listener
    }
}

data class StartRideData(
    val timeRemainingForPassenger: String,
    val passengerDroppedErrorLabel: String?,
    val timeLeftToPickPassengerInfo: String,
    val passengerInfo: String
)