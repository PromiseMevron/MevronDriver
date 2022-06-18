package com.mevron.rides.driver.cashout.ui.widgets.balancedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.R

class BalanceDetailsTopView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var balance: TextView
    private var dueDate: TextView
    private var cashOut: MaterialButton

    init {
        LayoutInflater.from(context).inflate(R.layout.balance_detail_top_layout, this, true)
        balance = findViewById(R.id.balance)
        dueDate = findViewById(R.id.due_date)
        cashOut = findViewById(R.id.cash_out)
    }

    fun setUpView(date: String, amount: String){
        balance.text = amount
        dueDate.text = "Your payout is scheduled on $date"
    }
}