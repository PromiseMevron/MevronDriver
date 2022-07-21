package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.data.model.home.WeeklySummary

class EarningWeeklyWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var earnings: TextView
    private var online: TextView
    private var rides: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.earning_weekly_layout, this, true)
        earnings = findViewById(R.id.earned_amount)
        online = findViewById(R.id.online_amount)
        rides = findViewById(R.id.rides_amount)
    }

    fun setUpData(data: WeeklySummary) {
        earnings.text = data.earnings
        online.text = data.online
        rides.text = "${data.rides}"

    }
}
