package com.mevron.rides.driver.cashout.ui.widgets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.data.model.BankInfo
import com.mevron.rides.driver.cashout.data.model.BankRawInfo
import com.mevron.rides.driver.cashout.domain.model.AddAccount
import com.mevron.rides.driver.databinding.AddAccountItemBinding

class AddAccountAdapter(val param: AccountFieldFilled):
    ListAdapter<AddAccount, AddAccountAdapter.AddAccountHolder>(AddAccountDiffUtil()) {

    class AddAccountHolder(val binding: AddAccountItemBinding): RecyclerView.ViewHolder(binding.root)

    class AddAccountDiffUtil: DiffUtil.ItemCallback<AddAccount>() {
        override fun areItemsTheSame(oldItem: AddAccount, newItem: AddAccount): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AddAccount, newItem: AddAccount): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddAccountHolder {
        return AddAccountHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.add_account_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddAccountHolder, position: Int) {
        val item = getItem(position)
        holder.binding.holderTitle.text = item.title
        holder.binding.holderField
        holder.binding.holderField.setOnFocusChangeListener { _, b ->
            val value = holder.binding.holderField.text.toString().trim()
            if (!b){
                param.parameterAdded(BankRawInfo(name = item.name, value = value, index = position))
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}

interface AccountFieldFilled{
    fun parameterAdded(data: BankRawInfo)
}

