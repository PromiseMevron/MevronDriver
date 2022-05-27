package com.mevron.rides.driver.cashout.ui.widgets.cashout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class CashOutTopView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.cashout_top_view_layout, this, true)

    }
}