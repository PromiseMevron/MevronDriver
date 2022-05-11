package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewHelpAndSupportBinding

class HelpAndSupportWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private lateinit var binding: ViewHelpAndSupportBinding

    init {
        LayoutInflater.from(context).inflate(R.layout.view_help_and_support, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding = ViewHelpAndSupportBinding.bind(this)
    }
}