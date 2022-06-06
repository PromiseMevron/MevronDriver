package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class LayoutError @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var errorLabel: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_error_widget, this, true)
        errorLabel = findViewById(R.id.errorLabel)
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }

    fun setErrorLabel(errorLabel: String) {
        this.errorLabel.text = errorLabel
    }
}