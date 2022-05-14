package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewLearningHubBinding
import com.mevron.rides.driver.widgets.viewBinding

class LearningHubWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_learning_hub, this, true)
    }

   private val binding by viewBinding(ViewLearningHubBinding::bind)
}