package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.mevron.rides.driver.R

class EmergencyWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr), View.OnClickListener {

    private var currentLocationText: TextView
    private var vehicleInfoText: TextView
    private var callNumberText: TextView
    private var onEmergencyButtonClickListener: OnEmergencyButtonClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_emergency_widget, this, true)
        currentLocationText = findViewById(R.id.yourCurrentLocationValue)
        vehicleInfoText = findViewById(R.id.vehicleDetails)
        callNumberText = findViewById(R.id.callNumberText)
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    private fun setCurrentLocation(text: String) {
        currentLocationText.text = text
    }

    private fun setVehicleInfo(info: String) {
        vehicleInfoText.text = info
    }

    fun setData(emergencyData: EmergencyData) {
        setCurrentLocation(emergencyData.currentLocation)
        setVehicleInfo(emergencyData.vehicleInfo)
    }

    fun setOnEmergencyButtonClickListener(listener: OnEmergencyButtonClickListener) {
        this.onEmergencyButtonClickListener = listener
    }

    fun setCallNumberText(text: String) {
        callNumberText.text = text
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.shareMyTrip -> onEmergencyButtonClickListener?.onShareMyTripClicked()
            R.id.callEmergencyNumber -> onEmergencyButtonClickListener?.onEmergencyCallClicked()
        }
    }
}

data class EmergencyData(
    val currentLocation: String,
    val vehicleInfo: String
)

interface OnEmergencyButtonClickListener {
    fun onShareMyTripClicked()
    fun onEmergencyCallClicked()
}