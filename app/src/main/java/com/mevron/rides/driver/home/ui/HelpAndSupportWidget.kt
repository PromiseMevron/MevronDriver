package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class HelpAndSupportWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private val topView: View
    private val bottomView: View
    private var eventClickListener: HelpAndSupportEventListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_help_and_support, this, true)
        topView = findViewById(R.id.top_box)
        bottomView = findViewById(R.id.bottom_box)
        topView.setOnClickListener {
            eventClickListener?.helpClicked()
        }

        bottomView.setOnClickListener {
            eventClickListener?.emergencyContactClicked()
        }
    }
    fun setUpClick(eventClickListener: HelpAndSupportEventListener){
        this.eventClickListener = eventClickListener
    }
}
interface HelpAndSupportEventListener{
    fun helpClicked()
    fun emergencyContactClicked()
}