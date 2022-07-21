package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.data.model.home.TodayActivityX
import com.mevron.rides.driver.trips.ui.RideActivitySubAdapter

class EarningToday @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var recyclerView: RecyclerView
    private var earnings: TextView
    private var online: TextView
    private var rides: TextView
    private var card: MaterialCardView
    private var empty: View

    init {
        LayoutInflater.from(context).inflate(R.layout.earning_today_layout, this, true)
       recyclerView = findViewById(R.id.recycler_view)
        earnings = findViewById(R.id.earned_amount)
        online = findViewById(R.id.online_amount)
          rides = findViewById(R.id.rides_amount)
          card = findViewById(R.id.card)
          empty = findViewById(R.id.empty_bottom)
    }

    fun setUpData(data: TodayActivityX){
        val adapter = RideActivitySubAdapter()
        recyclerView.adapter = adapter
        earnings.text = "${data.earnings}"
        online.text = data.online
        rides.text = "${data.rides}"
        adapter.submitList(data.trip_list.take(3))
        if (data.trip_list.isEmpty()){
            card.visibility = VISIBLE
            empty.visibility = VISIBLE
            recyclerView.visibility = GONE
        }else{
            card.visibility = GONE
            empty.visibility = GONE
            recyclerView.visibility = VISIBLE
        }
    }


}
