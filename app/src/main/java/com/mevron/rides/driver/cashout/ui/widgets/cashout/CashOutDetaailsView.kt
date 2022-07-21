package com.mevron.rides.driver.cashout.ui.widgets.cashout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R


class CashOutDetaailsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var earning: TextView
    private var other: TextView
    private var tips: TextView
    private var total: TextView
    private var fee: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.cash_out_detail_layout, this, true)
        earning = findViewById(R.id.earning_detail)
        other = findViewById(R.id.other_detail)
        tips = findViewById(R.id.tip_detail)
        total = findViewById(R.id.total_detail)
        fee = findViewById(R.id.fee_detail)
    }

    fun setUpDetailsView(totalAmount: String){
        total.text = totalAmount
    }
}