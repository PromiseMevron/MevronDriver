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
    private var passengerContact: PassengerContactWidet

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

    fun setSlideCompleteCallback(listener: SlideToActView.OnSlideCompleteListener) {
        startRideButton.onSlideCompleteListener = listener
    }
}