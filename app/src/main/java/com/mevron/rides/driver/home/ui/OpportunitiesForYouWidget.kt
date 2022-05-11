package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewOpportunityForYouBinding

class OpportunitiesForYouWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private lateinit var binding: ViewOpportunityForYouBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.view_opportunity_for_you, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ViewOpportunityForYouBinding.bind(this)
    }
}