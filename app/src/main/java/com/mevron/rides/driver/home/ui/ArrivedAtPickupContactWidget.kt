package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class ArrivedAtPickupWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {

    private var arrivedActionButton: ImageButton
    private var callButton: View
    private var messageButton: View
    private var passengerAvatar: ImageView
    private var ratingText: TextView
    private var ratingBackground: View
    private var passengerName: TextView
    private var onContactClickedListener: OnContactClickedListener? = null
    private var onArriveButtonClickedListener: OnArriveButtonClickedListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_arrived_at_passenger, this, true)
        arrivedActionButton = findViewById(R.id.arrivedButton)
        callButton = findViewById(R.id.callBackground)
        messageButton = findViewById(R.id.messageBackground)
        passengerAvatar = findViewById(R.id.passengerAvatar)
        ratingBackground = findViewById(R.id.ratingBackground)
        ratingText = findViewById(R.id.ratingText)
        passengerName = findViewById(R.id.passengerName)
        arrivedActionButton.setOnClickListener(this)
    }

    fun setPassengerName(passengerName: String) {
        this.passengerName.text = passengerName
    }

    fun setRating(rating: String) {
        ratingText.text = rating
    }

    fun setImage(url: String) {

    }

    fun bind(data: ArrivedData) {
        setPassengerName(data.passengerName)
        setImage(data.passengerImage)
        setRating(data.rating)
    }

    fun setOnContactClickedListener(listener: OnContactClickedListener) {
        this.onContactClickedListener = listener
    }

    fun setOnArriveButtonClickListener(listener: OnArriveButtonClickedListener) {
        this.onArriveButtonClickedListener = listener
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.callBackground -> onContactClickedListener?.onCallClicked()
            R.id.messageBackground -> onContactClickedListener?.onMessageClicked()
            R.id.arrivedButton -> onArriveButtonClickedListener?.onArriveClicked()
        }
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }
}

interface OnContactClickedListener {
    fun onCallClicked()
    fun onMessageClicked()
}

interface OnArriveButtonClickedListener {
    fun onArriveClicked()
}

data class ArrivedData(val passengerName: String, val rating: String, val passengerImage: String)