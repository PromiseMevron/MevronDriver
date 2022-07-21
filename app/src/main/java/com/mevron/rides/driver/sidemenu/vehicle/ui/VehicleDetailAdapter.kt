package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentItemBinding
import com.mevron.rides.driver.documentcheck.data.model.Document

class VehicleDetailAdapter(val sel: SelectVehicleDetail, val context: Context) :
    ListAdapter<Document, VehicleDetailAdapter.VehiHolder>(
        VehicleDetailDiffUtil()
    ) {

    class VehicleDetailDiffUtil : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    class VehiHolder(val binding: DocumentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.document_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        val doc = getItem(position)
        holder.binding.carDoc.text = doc.name
        when (setUpDocView(doc.status)) {
            DocStatus.Okay -> {
                holder.binding.docStatus.visibility = View.GONE
                holder.binding.backGround.setBackgroundColor(
                    context.resources.getColor(
                        R.color.document_ok,
                        null
                    )
                )
            }
            DocStatus.Pending -> {
                holder.binding.docStatus.visibility = View.VISIBLE
                holder.binding.backGround.setBackgroundColor(
                    context.resources.getColor(
                        R.color.document_under_review,
                        null
                    )
                )
                holder.binding.docStatus.text = context.getString(R.string.under_review)
            }
            DocStatus.None -> {
                holder.binding.docStatus.visibility = View.VISIBLE
                holder.binding.backGround.setBackgroundColor(
                    context.resources.getColor(
                        R.color.document_pending,
                        null
                    )
                )
                holder.binding.docStatus.text =
                    "${context.getString(R.string.upload_photo)} ${doc.name}"
            }
        }
        holder.binding.root.setOnClickListener {
            sel.selectVehicle()
        }
    }

    fun setUpDocView(status: Int): DocStatus {
        return if (status == 0)
            DocStatus.None
        else if (status == 1)
            DocStatus.Pending
        else
            DocStatus.Okay
    }
}

interface SelectVehicleDetail {
    fun selectVehicle()
}

enum class DocStatus {
    Okay,
    Pending,
    None
}