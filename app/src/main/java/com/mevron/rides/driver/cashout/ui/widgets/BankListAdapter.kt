package com.mevron.rides.driver.cashout.ui.widgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.BankList
import com.mevron.rides.driver.databinding.BankListItemBinding

class BankListAdapter(private val selected: BankSelected) :
    ListAdapter<BankList, BankListHolder>(BankListDiffUtil()) {

    class BankListDiffUtil : DiffUtil.ItemCallback<BankList>() {
        override fun areItemsTheSame(
            oldItem: BankList,
            newItem: BankList
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BankList,
            newItem: BankList
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListHolder {
        return BankListHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bank_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BankListHolder, position: Int) {
        val item = getItem(position)
        holder.binding.typeName.text = item.name
        holder.binding.root.setOnClickListener {
            selected.selectedBank(item)
        }
    }
}

class BankListHolder(val binding: BankListItemBinding) :
    RecyclerView.ViewHolder(binding.root)

interface BankSelected {
    fun selectedBank(bank: BankList)
}
