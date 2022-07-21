package com.mevron.rides.driver.sidemenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.PayTypeItemBinding
import com.mevron.rides.driver.remote.model.getcard.CardData

class PaymentAdapter2(val paySelected: PaySelected2) :
    ListAdapter<CardData, PaymentAdapter2.PayHolder>(PaymentAdapterDiffUtil()) {

    class PayHolder(val binding: PayTypeItemBinding) : RecyclerView.ViewHolder(binding.root)

    class PaymentAdapterDiffUtil : DiffUtil.ItemCallback<CardData>() {
        override fun areItemsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardData, newItem: CardData): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayHolder {
        return PayHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.pay_type_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PayHolder, position: Int) {
        val dt = getItem(position)
        holder.binding.image.setImageResource(R.drawable.ic_mastercard)
        holder.binding.typeName.text = "****" + dt.lastDigits
        holder.binding.next.visibility = View.VISIBLE
        holder.binding.root.setOnClickListener {
            paySelected.selected(dt)
        }

    }
}

interface PaySelected2 {
    fun selected(data: CardData)
}