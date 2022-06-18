package com.mevron.rides.driver.cashout.ui.widgets.cashout

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R

class CashOutTopView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {

    private var owned: TextView
    private var backButton: ImageButton
    private var account: TextView
    private var bank: TextView
    private var bankLogo: ImageView
    lateinit var backClicked: TopLayerButtonClicked

    init {
        LayoutInflater.from(context).inflate(R.layout.cashout_top_view_layout, this, true)
        backButton = findViewById(R.id.back_button)
        owned = findViewById(R.id.owned_title)
        account = findViewById(R.id.number)
        bank = findViewById(R.id.bankName)
        bankLogo = findViewById(R.id.bank_logo)
    }

    fun setUpTopView(balance: String){
        owned.text = balance
        backButton.setOnClickListener {
            backClicked.clickedBackButton()
        }
    }
}
interface TopLayerButtonClicked{
    fun clickedBackButton()
}