package com.mevron.rides.driver.home.ui

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.mevron.rides.driver.R
import com.squareup.picasso.Picasso

class RatingRiderWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var riderImageView: ImageView
    private var doneButton: ImageButton
    private var cancelButton: ImageButton
    private var ratingText: TextView
    private var amountText: TextView
    private var theRating: AppCompatRatingBar
    private var listener: RatingEventListener? = null


    init {
        LayoutInflater.from(context).inflate(R.layout.rating_layout, this, true)
        ratingText = findViewById(R.id.customer_to_pay)
        amountText = findViewById(R.id.customer_to_pay_amount)
        doneButton = findViewById(R.id.done_rating)
        cancelButton = findViewById(R.id.close_rating)
        riderImageView = findViewById(R.id.rider_profile_image)
        theRating = findViewById(R.id.rating)

        doneButton.setOnClickListener {
            val rate = theRating.rating
            if (rate > 1) {
                listener?.ratingScore(rate.toString())
            }else{
                listener?.ratingScore("1")
            }
        }
        cancelButton.setOnClickListener {
            listener?.ratingScore("1")
        }
    }

    fun setData(data: PayData) {
        ratingText.text = "Rate your experience with ${data.name}?"
        amountText.text = "Ride Amount: ${data.currency}${data.amount}"
        if (!data.image.isNullOrEmpty())
            Picasso.get().load(Uri.parse(data.image)).into(riderImageView)
    }

    fun setListener(listener: RatingEventListener) {
        this.listener = listener
    }

    fun show() {
        visibility = VISIBLE
    }

    fun hide() {
        visibility = GONE
    }
}

interface RatingEventListener {
    fun ratingScore(rating: String)
}