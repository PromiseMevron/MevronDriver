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

class AcceptRideView @JvmOverloads constructor(
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
    private lateinit var status: TripStatus

    private var onActionButtonClick: OnActionButtonClick? = null

    private var onStatusChangedListener: OnStatusChangedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_accept_ride_view, this, true)
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

    private fun setLeftCellText(text: String) {
        rideDuration.text = text
    }

    fun initStatus(status: TripStatus) {
        this.status = status
    }

    private fun setInfoText(text: String) {
        tripInfoText.text = text
    }

    private fun setRightCellText(text: String) {
        distanceRemainingLabel.text = text
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    fun bindData(data: AcceptRideData) {
        setLeftCellText(data.rideDuration)
        setInfoText(data.tripInfo)
        setRightCellText(data.distanceRemaining)
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
//                when (status) {
//                    TripStatus.TO_ACCEPT -> onStatusChangedListener?.onNewStatus(TripStatus.IN_TRIP)
//                    TripStatus.IN_TRIP -> onStatusChangedListener?.onNewStatus(TripStatus.PAYMENT)
//                    TripStatus.PAYMENT -> {}
//                }
                onActionButtonClick?.onActionButtonClick()
            }
        }
    }

    fun renderTripProgress(tripProgress: TripProgressUpdateValue) {
        show()
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

data class AcceptRideData(
    val passengerImage: String,
    val tripInfo: String,
    val rideDuration: String,
    val distanceRemaining: String
)

interface OnStatusChangedListener {
    fun onNewStatus(status: TripStatus)
}

interface OnActionButtonClick {
    fun onActionButtonClick()
}