package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mevron.rides.driver.R

class DocumentSubmissionPendingWidget @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr) {
    private var attentionMessageTitle: TextView
    private var attentionMessageSubTitle: TextView
    private var background: ConstraintLayout

    init {
        LayoutInflater.from(context).inflate(R.layout.view_document_submission_pending, this, true)
        attentionMessageTitle = findViewById(R.id.attention_message_title)
        attentionMessageSubTitle = findViewById(R.id.attention_message_sub_title)
        background = findViewById(R.id.back_ground)
    }

  //  private val binding by viewBinding(ViewDocumentSubmissionPendingBinding::bind)

    fun hideView() {
        visibility = View.GONE
    }

    fun showView() {
        visibility = View.VISIBLE
    }

    private fun renderPending() {
        showView()
        background.setBackgroundColor(ContextCompat.getColor(context, R.color.grey_7))
        attentionMessageTitle.text =
            context.getString(R.string.documents_submission_pending)
        attentionMessageSubTitle.visibility = View.VISIBLE
    }

    private fun renderReview() {
        showView()
        background.setBackgroundColor(ContextCompat.getColor(context, R.color.document_under_review))
        attentionMessageTitle.text =
            context.getString(R.string.documents_submission_under_review)
        attentionMessageSubTitle.visibility = View.GONE
    }

    private fun renderRejected() {
        showView()
        background.setBackgroundColor(ContextCompat.getColor(context, R.color.document_rejected))
        attentionMessageTitle.text =
            context.getString(R.string.the_details_you_submitted_are_invalid_or_incorrect_hence_wasn_t_approved_please_resubmit_your_details)
        attentionMessageSubTitle.visibility = View.VISIBLE
    }

    private fun renderExpired() {
        showView()
        background.setBackgroundColor(ContextCompat.getColor(context, R.color.document_rejected))
        attentionMessageTitle.text =
            context.getString(R.string.expired_doument)
        attentionMessageSubTitle.visibility = View.VISIBLE
    }

    fun toggleStatus(documentSubmissionStatus: DocumentSubmissionStatus) =
        when (documentSubmissionStatus) {
            DocumentSubmissionStatus.NONE -> renderPending()
            DocumentSubmissionStatus.REVIEW -> renderReview()
            DocumentSubmissionStatus.OKAY -> hideView()
            DocumentSubmissionStatus.REJECTED -> renderRejected()
            DocumentSubmissionStatus.EXPIRED -> renderExpired()
        }
}

enum class DocumentSubmissionStatus {
    NONE, REVIEW, OKAY, REJECTED, EXPIRED
}