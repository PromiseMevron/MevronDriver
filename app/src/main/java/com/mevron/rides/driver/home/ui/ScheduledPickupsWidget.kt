package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewScheduledPickupsBinding

class ScheduledPickupsWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private lateinit var binding: ViewScheduledPickupsBinding
    init {
        LayoutInflater.from(context).inflate(R.layout.view_scheduled_pickups, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ViewScheduledPickupsBinding.bind(this)
    }
}