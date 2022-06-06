package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.mevron.rides.driver.R
import com.ncorti.slidetoact.SlideToActView

class GoingToDestinationWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    private var timeToDestination: TextView
    private var actionText: TextView
    private var completeRideButton: SlideToActView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_going_to_destination, this, true)
        actionText = findViewById(R.id.actionText)
        timeToDestination = findViewById(R.id.timeToDestination)
        completeRideButton = findViewById(R.id.completeRideButton)
    }

    fun setTimeToDestination(timeToDestination: String) {
        this.timeToDestination.text = timeToDestination
    }

    fun setActionText(actionText: String) {
        this.actionText.text = actionText
    }

    fun setSlideCompleteListener(onSlideComplete: SlideToActView.OnSlideCompleteListener) {
        completeRideButton.onSlideCompleteListener = onSlideComplete
    }
}