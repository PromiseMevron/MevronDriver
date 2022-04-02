package com.mevron.rides.driver.sidemenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.PayTypeItemBinding
import com.mevron.rides.driver.remote.model.getcard.Data


class PaymentAdapter2(val paySelected: PaySelected2, val data: List<Data>): RecyclerView.Adapter<PaymentAdapter2.PayHolder>() {


    class PayHolder(val binding: PayTypeItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayHolder {
        return PayHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.pay_type_item, parent, false))

    }

    override fun onBindViewHolder(holder: PayHolder, position: Int) {

            val dt = data[position]
            holder.binding.image.setImageResource(R.drawable.ic_mastercard)
            holder.binding.typeName.text = "****" + dt.lastDigits
            holder.binding.next.visibility = View.VISIBLE
            holder.binding.root.setOnClickListener {
                paySelected.selected(dt)
            }

    }

    override fun getItemCount(): Int {
        return  data.size
    }
}

interface PaySelected2{
    fun selected(data: Data)
}