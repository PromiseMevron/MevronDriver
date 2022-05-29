package com.mevron.rides.driver.home.map.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mapbox.navigation.ui.tripprogress.model.TripProgressUpdateValue
import com.mevron.rides.driver.R
import douglasspgyn.com.github.circularcountdown.CircularCountdown

class TripView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {

    private lateinit var circularCountdown: CircularCountdown
    private lateinit var profilePhoto: ImageView
    private lateinit var tripInfoText: TextView
    private lateinit var rideType: ImageView
    private lateinit var rideDuration: TextView
    private lateinit var distanceRemainingLabel: TextView
    private lateinit var actionButton: Button
    private lateinit var status: Status

    private var onActionButtonClick: OnActionButtonClick? = null

    private var onStatusChangedListener: OnStatusChangedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_trip_view, this, true)
        setUpView()
    }

    private fun setUpView() {
        circularCountdown = findViewById(R.id.circularCountdown)
        profilePhoto = findViewById(R.id.profile_photo)
        tripInfoText = findViewById(R.id.trip_info_text)
        rideType = findViewById(R.id.rideTypeImage)
        rideDuration = findViewById(R.id.ride_duration)
        distanceRemainingLabel = findViewById(R.id.distance_remaining_label)
        actionButton = findViewById(R.id.map_trip_view_action_button)
        actionButton.setOnClickListener(this)
    }

    fun setLeftCellText(text: String) {
        rideDuration.text = text
    }

    fun initStatus(status: Status) {
        this.status = status
    }

    fun setInfoText(text: String) {
        tripInfoText.text = text
    }

    fun setRightCellText(text: String, bufferType: TextView.BufferType) {
        distanceRemainingLabel.text = text
    }

    fun setOnActionClick(onActionButtonClick: OnActionButtonClick) {
        this.onActionButtonClick = onActionButtonClick
    }

    fun setOnStatusChangedListener(onStatusChangedListener: OnStatusChangedListener) {
        this.onStatusChangedListener = onStatusChangedListener
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.map_trip_view_action_button -> {
                when(status) {
                    Status.TO_ACCEPT -> onStatusChangedListener?.onNewStatus(Status.IN_TRIP)
                    Status.IN_TRIP -> onStatusChangedListener?.onNewStatus(Status.PAYMENT)
                    Status.PAYMENT -> {}
                }
                onActionButtonClick?.onActionButtonClick()
            }
        }
    }

    fun renderTripProgress(tripProgress: TripProgressUpdateValue) {
        rideDuration.setText(
            tripProgress.formatter.getTimeRemaining(tripProgress.currentLegTimeRemaining),
            TextView.BufferType.SPANNABLE
        )

        tripInfoText.setText(
            tripProgress.formatter.getTimeRemaining(tripProgress.totalTimeRemaining),
            TextView.BufferType.SPANNABLE
        )

        distanceRemainingLabel.setText(
            tripProgress.formatter.getDistanceRemaining(tripProgress.distanceRemaining),
            TextView.BufferType.SPANNABLE
        )
    }
}

enum class Status {
   TO_ACCEPT, IN_TRIP, PAYMENT
}

interface OnStatusChangedListener {
    fun onNewStatus(status: Status)
}

interface OnActionButtonClick {
    fun onActionButtonClick()
}