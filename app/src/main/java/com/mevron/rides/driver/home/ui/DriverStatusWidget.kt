package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.ui.widgeteventlisteners.DriverStatusClickListener

class DriverStatusWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {

    private var earnings: ImageView
    private var drive: ImageView
    private var goOnline: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_driver_status, this, true)
        earnings = findViewById(R.id.earnings)
        drive = findViewById(R.id.drive)
        goOnline = findViewById(R.id.go_online)
        earnings.setOnClickListener(this)
        drive.setOnClickListener(this)
        goOnline.setOnClickListener(this)
    }

    private var eventListener: DriverStatusClickListener? = null

    fun toggleDrive(shouldToggleDrive: Boolean) {
        if (shouldToggleDrive) {
            earnings.setImageResource(R.drawable.ic_earning_un)
            drive.setImageResource(R.drawable.ic_drive)
        } else {
            earnings.setImageResource(R.drawable.ic_earning)
            drive.setImageResource(R.drawable.ic_drive_un)
        }
    }

    fun setClickEventListener(eventListener: DriverStatusClickListener) {
        this.eventListener = eventListener
    }

    fun toggleOnlineStatus(isOnline: Boolean) {
        if (isOnline) {
            goOnline.setImageResource(R.drawable.ic_go_online)
        } else {
            goOnline.setImageResource(R.drawable.ic_offline)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.drive -> {
                eventListener?.onDriveClick()
            }
            R.id.earnings -> {
                eventListener?.onEarningClick()
            }

            R.id.go_online -> {
                eventListener?.goOnlineClick()
            }
        }
    }
}