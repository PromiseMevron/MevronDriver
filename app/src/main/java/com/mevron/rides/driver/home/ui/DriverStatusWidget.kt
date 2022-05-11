package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewDriverStatusBinding
import com.mevron.rides.driver.home.ui.widgeteventlisteners.DriverStatusClickListener

class DriverStatusWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_driver_status, this, true)
    }

    private var eventListener: DriverStatusClickListener? = null

    private lateinit var binding: ViewDriverStatusBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ViewDriverStatusBinding.bind(this)
    }

    fun toggleDrive(shouldToggleDrive: Boolean) {
        if (shouldToggleDrive) {
            binding.earnings.setImageResource(R.drawable.ic_earning_un)
            binding.drive.setImageResource(R.drawable.ic_drive)
        } else {
            binding.earnings.setImageResource(R.drawable.ic_earning)
            binding.drive.setImageResource(R.drawable.ic_drive_un)
        }
    }

    fun setClickEventListener(eventListener: DriverStatusClickListener) {
        this.eventListener = eventListener
    }

    fun toggleOnlineStatus(isOnline: Boolean) {
        if (isOnline) {
            binding.goOnline.setImageResource(R.drawable.ic_go_online)
        } else {
            binding.goOnline.setImageResource(R.drawable.ic_offline)
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