package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.GetBankDatum
import com.mevron.rides.driver.databinding.BankItemsBinding

class BanksAdapter(val paySelected: BankSelected) :
    ListAdapter<GetBankDatum, BanksAdapter.BankHolder>(BankAdapterDiffUtil()) {

    class BankHolder(val binding: BankItemsBinding) : RecyclerView.ViewHolder(binding.root)

    class BankAdapterDiffUtil : DiffUtil.ItemCallback<GetBankDatum>() {
        override fun areItemsTheSame(oldItem: GetBankDatum, newItem: GetBankDatum): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GetBankDatum, newItem: GetBankDatum): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankHolder {
        return BankHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bank_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BankHolder, position: Int) {
        val dt = getItem(position)

        val digits = if (dt.account_number.length == 4) {
            dt.account_number
        } else if (dt.account_number.length > 4) {
            dt.account_number.substring(dt.account_number.length - 4);
        } else {
            dt.account_number
        }
        holder.binding.image.setImageResource(R.drawable.ic_bank_add)
        holder.binding.typeDetail.text = "****$digits"
        holder.binding.typeName.text = dt.bank_name
        holder.binding.next.visibility = View.VISIBLE
        holder.binding.root.setOnClickListener {
            paySelected.selectedBank(dt)
        }

    }
}

interface BankSelected {
    fun selectedBank(data: GetBankDatum)
}