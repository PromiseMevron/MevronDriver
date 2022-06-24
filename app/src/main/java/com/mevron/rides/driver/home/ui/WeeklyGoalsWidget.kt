package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewWeeklyGoalsBinding
import com.mevron.rides.driver.home.data.model.home.WeeklyChallenge
import com.mevron.rides.driver.home.ui.adapter.WeeklyChallengeAdapter
import com.mevron.rides.driver.widgets.viewBinding

class WeeklyGoalsWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var recyclerView: RecyclerView
    private var card: MaterialCardView
    private var adapter: WeeklyChallengeAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.view_weekly_goals, this, true)
        recyclerView = findViewById(R.id.weekly_recycler)
        card = findViewById(R.id.card)
        adapter = WeeklyChallengeAdapter()
    }
    private val binding by viewBinding(ViewWeeklyGoalsBinding::bind)

    fun bindData(data: List<WeeklyChallenge>){
        recyclerView.adapter = adapter
        adapter.submitList(data)
        if (data.isEmpty()){
            recyclerView.visibility = View.GONE
            card.visibility = View.VISIBLE
        }else{
            recyclerView.visibility = View.VISIBLE
            card.visibility = View.GONE
        }

    }
}
