package com.mevron.rides.driver.cashout.ui.widgets.balancedetails

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.cashout.ui.widgets.BalanceAdapter

class BalanceDetaillsDetails @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var recyclerView: RecyclerView
    init {
        LayoutInflater.from(context).inflate(R.layout.balance_details_view, this, true)
        recyclerView = findViewById(R.id.recycler_view)
    }
    fun setUpAdapter(adapter: BalanceAdapter, data: List<PaymentBalanceDetailsDomainDatum>){
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter.submitList(data)
    }
}