package com.mevron.rides.driver.home.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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

    init {
        LayoutInflater.from(context).inflate(R.layout.view_document_submission_pending, this, true)
    }

    private val binding by viewBinding(ViewDocumentSubmissionPendingBinding::bind)

    fun hideView() {
        visibility = View.GONE
    }

    fun showView() {
        visibility = View.VISIBLE
    }

    private fun renderPending() {
        showView()
        setBackgroundColor(ContextCompat.getColor(context, R.color.document_pending))
        binding.attentionMessageTitle.text =
            context.getString(R.string.documents_submission_pending)
        binding.attentionMessageSubTitle.visibility = View.VISIBLE
    }

    private fun renderReview() {
        showView()
        setBackgroundColor(ContextCompat.getColor(context, R.color.document_under_review))
        binding.attentionMessageTitle.text =
            context.getString(R.string.documents_submission_under_review)
        binding.attentionMessageSubTitle.visibility = View.GONE
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