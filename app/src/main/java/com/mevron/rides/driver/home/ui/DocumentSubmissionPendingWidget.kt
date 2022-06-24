package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ViewDocumentSubmissionPendingBinding
import com.mevron.rides.driver.widgets.viewBinding

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
        background.setBackgroundColor(ContextCompat.getColor(context, R.color.document_pending))
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

    fun toggleStatus(documentSubmissionStatus: DocumentSubmissionStatus) =
        when (documentSubmissionStatus) {
            DocumentSubmissionStatus.PENDING -> renderPending()
            DocumentSubmissionStatus.REVIEW -> renderReview()
            DocumentSubmissionStatus.OKAY -> hideView()
        }
}

enum class DocumentSubmissionStatus {
    PENDING, REVIEW, OKAY
}