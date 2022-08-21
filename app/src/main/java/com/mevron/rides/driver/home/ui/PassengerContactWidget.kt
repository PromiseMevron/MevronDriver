package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class PassengerContactWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {
    private var onContactClickedListener: OnContactClickedListener? = null
    private var callImage: View
    private var messageImage: View
    private var passengerLabel: TextView
    private var timeRemainingLabel: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_going_to_passenger, this, true)
        callImage = findViewById(R.id.callBackground)
        messageImage = findViewById(R.id.messageBackground)
        passengerLabel = findViewById(R.id.passengerMessage)
        timeRemainingLabel = findViewById(R.id.timeRemaining)
    }

    fun setContactClickListener(listener: OnContactClickedListener) {
        this.onContactClickedListener = listener
    }

    private fun setTimeRemainingLabel(text: String) {
        timeRemainingLabel.text = text
    }

    private fun setPassengerLabel(text: String) {
        passengerLabel.text = text
    }

    fun bind(data: OnTheWayToPassengerData) {
        setTimeRemainingLabel(data.timeLeftInfo)
        setPassengerLabel(data.pickUpPassengerInfo)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.callBackground -> onContactClickedListener?.onCallClicked()
            R.id.messageBackground -> onContactClickedListener?.onMessageClicked()
        }
    }

    fun hide() {
        visibility = GONE
    }

    fun show() {
        visibility = VISIBLE
    }
}

data class OnTheWayToPassengerData(val timeLeftInfo: String, val pickUpPassengerInfo: String)