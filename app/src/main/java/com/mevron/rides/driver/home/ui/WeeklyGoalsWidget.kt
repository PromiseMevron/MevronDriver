package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewWeeklyGoalsBinding

class WeeklyGoalsWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_weekly_goals, this, true)
    }

    private lateinit var binding: ViewWeeklyGoalsBinding

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ViewWeeklyGoalsBinding.bind(this)
    }
}
