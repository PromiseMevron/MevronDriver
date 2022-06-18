package com.mevron.rides.driver.cashout.ui.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.databinding.BalanceDetailsDetailsScreenBinding

class BalanceAdapter(val context: Context) :
    ListAdapter<PaymentBalanceDetailsDomainDatum, BalanceHolder>(BalanceDiffUtil()) {

    class BalanceDiffUtil : DiffUtil.ItemCallback<PaymentBalanceDetailsDomainDatum>() {
        override fun areItemsTheSame(
            oldItem: PaymentBalanceDetailsDomainDatum,
            newItem: PaymentBalanceDetailsDomainDatum
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PaymentBalanceDetailsDomainDatum,
            newItem: PaymentBalanceDetailsDomainDatum
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceHolder {
        return BalanceHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.balance_details_details_screen,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BalanceHolder, position: Int) {
        val item = getItem(position)
        holder.binding.date.text = item.date
        holder.binding.recyclerView.setHasFixedSize(true)
        val adapter = BalanceSubAdapter(context = context)
        holder.binding.recyclerView.adapter = adapter
        adapter.submitList(item.data)
    }
}

class BalanceHolder(val binding: BalanceDetailsDetailsScreenBinding) :
    RecyclerView.ViewHolder(binding.root)
