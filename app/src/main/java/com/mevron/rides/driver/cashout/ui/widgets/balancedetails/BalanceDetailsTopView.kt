package com.mevron.rides.driver.cashout.ui.widgets.balancedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.ui.OnEarningCashOutButtonClickListener

class BalanceDetailsTopView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {
    private var balance: TextView
    private var dueDate: TextView
    private var cashOut: ImageButton
    private var addFund: ImageButton
    private var backButton: ImageButton
    private var onBalanceDetailButtonClickListener: OnBalanceDetailButtonClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.balance_detail_top_layout, this, true)
        balance = findViewById(R.id.balance)
        dueDate = findViewById(R.id.due_date)
        cashOut = findViewById(R.id.cash_out)
        backButton = findViewById(R.id.back_button)
        addFund = findViewById(R.id.add_fund)
        cashOut.setOnClickListener(this)
        addFund.setOnClickListener(this)
        backButton.setOnClickListener(this)
    }

    fun setUpView(date: String, amount: String){
        balance.text = amount
        dueDate.text = "Your payout is scheduled on $date"
    }
    fun setEventsClickListener(listener: OnBalanceDetailButtonClickListener) {
        onBalanceDetailButtonClickListener = listener
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cash_out -> onBalanceDetailButtonClickListener?.onCashOutClicked()
            R.id.add_fund -> onBalanceDetailButtonClickListener?.onDetailOutClicked()
            R.id.back_button -> onBalanceDetailButtonClickListener?.onCashOutBackClicked()

        }
    }
}

interface OnBalanceDetailButtonClickListener {
    fun onCashOutClicked()
    fun onDetailOutClicked()
    fun onCashOutBackClicked()
}