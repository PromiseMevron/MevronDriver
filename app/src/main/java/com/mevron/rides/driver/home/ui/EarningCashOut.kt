package com.mevron.rides.driver.home.ui


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.data.model.home.Earnings

class EarningCashOut @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), View.OnClickListener {
    private var balance: TextView
    private var payout: TextView
    private var cashOut: ImageButton
    private var details: ImageButton
    private var onEarningCashOutButtonClickListener: OnEarningCashOutButtonClickListener? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.earning_cashout_layout, this, true)
        balance = findViewById(R.id.balance)
        payout = findViewById(R.id.due_date)
        cashOut = findViewById(R.id.cash_out)
        details = findViewById(R.id.details)
        cashOut.setOnClickListener(this)
        details.setOnClickListener(this)
    }

    fun setEventsClickListener(listener: OnEarningCashOutButtonClickListener) {
        onEarningCashOutButtonClickListener = listener
    }

    @SuppressLint("SetTextI18n")
    fun setUpData(data: Earnings) {
        balance.text = "${data.currency}${data.balance}"
        payout.text = "Your payout is scheduled on ${data.nextPaymentDate}"
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.cash_out -> onEarningCashOutButtonClickListener?.onCashOutClicked()
            R.id.details -> onEarningCashOutButtonClickListener?.onDetailOutClicked()
        }
    }
}

interface OnEarningCashOutButtonClickListener {
    fun onCashOutClicked()
    fun onDetailOutClicked()
}
