package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.ncorti.slidetoact.SlideToActView

class StartRideWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), SlideToActView.OnSlideCompleteListener {

    private var passengerOut: LayoutError
    private var timeRemainingForPassenger: TextView
    private var codeText: EditText
    private var codeTextLayout: ConstraintLayout
    private var verify: Button
    private var back: ImageButton
    private var startRideButton: SlideToActView
    private var passengerContact: PassengerContactWidget
    private var listener: AcceptSlideCompleteListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_start_the_ride, this, true)
        passengerOut = findViewById(R.id.passengerOut)
        timeRemainingForPassenger = findViewById(R.id.timeRemainingForPassengerArrival)
        startRideButton = findViewById(R.id.startRideButton)
        passengerContact = findViewById(R.id.passengerContact)
        codeText = findViewById(R.id.code)
        codeTextLayout = findViewById(R.id.code_layout)
        verify = findViewById(R.id.submit)
        back = findViewById(R.id.back_button)
        startRideButton.onSlideCompleteListener = this
        verify.setOnClickListener {
            if (codeText.text.toString().isNotEmpty()){
                codeTextLayout.visibility = GONE
                listener?.startRide(codeText.text.toString())
                codeText.setText("")
            }
        }
        back.setOnClickListener {
            codeTextLayout.visibility = GONE
            codeText.setText("")
        }
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

    fun setSlideCompleteCallback(listener: AcceptSlideCompleteListener) {
        this.listener = listener
    }

    override fun onSlideComplete(view: SlideToActView) {
       codeTextLayout.visibility = VISIBLE

    }
}

data class StartRideData(
    val timeRemainingForPassenger: String,
    val passengerDroppedErrorLabel: String?,
    val timeLeftToPickPassengerInfo: String = "",
    val passengerInfo: String
)

interface AcceptSlideCompleteListener{
    fun startRide(code: String)
}