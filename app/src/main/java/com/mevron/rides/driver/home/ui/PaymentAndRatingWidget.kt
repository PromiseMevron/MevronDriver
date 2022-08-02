package com.mevron.rides.driver.home.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.R
import com.squareup.picasso.Picasso

class PaymentAndRatingWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var backButton: ImageButton
    private var collectButton: MaterialButton
    private var doneButton: ImageButton
    private var collectText: TextView
    private var collectAmountText: TextView
    private var amountText: EditText
    private var includedPayLayout: View
    private var includedTipLayout: View
    private var riderImageView: ImageView
    private var defaultAmount: Int = 0
    private var listener: PaymentWidgetEventListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.pay_and_tip_layout, this, true)
        includedPayLayout = findViewById(R.id.payment_screen)
        includedTipLayout = findViewById(R.id.payment_tip_screen)
        backButton = includedPayLayout.findViewById(R.id.back_button)
        collectButton = includedPayLayout.findViewById(R.id.cash_collected)
        collectText = includedPayLayout.findViewById(R.id.customer_to_pay)
        collectAmountText = includedPayLayout.findViewById(R.id.customer_amount)
        doneButton = includedTipLayout.findViewById(R.id.done_button)
        amountText = includedTipLayout.findViewById(R.id.amount)
        riderImageView = includedPayLayout.findViewById(R.id.rider_profile_image)

        collectButton.setOnClickListener {
            includedPayLayout.visibility = GONE
            includedTipLayout.visibility = VISIBLE
        }

        doneButton.setOnClickListener {
            if (amountText.text.toString().isNotEmpty() && amountText.text.toString()
                    .toInt() >= defaultAmount
            ) {
                listener?.cashCollected(amountText.text.toString())
                includedPayLayout.visibility = VISIBLE
                includedTipLayout.visibility = GONE
            } else {
                listener?.errorOnCashCollected()
            }
        }

    }

    fun setData(data: PayData) {
        collectText.text = "Collect Cash from ${data.name}"
        collectAmountText.text = data.amount
        defaultAmount = data.amount.toInt()
        if (!data.image.isNullOrEmpty())
        Picasso.get().load(Uri.parse(data.amount)).into(riderImageView)
    }

    fun setListener(listener: PaymentWidgetEventListener) {
        this.listener = listener
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }


}

interface PaymentWidgetEventListener {
    fun cashCollected(amount: String)
    fun errorOnCashCollected()
}

data class PayData(val image: String, val name: String, val amount: String)