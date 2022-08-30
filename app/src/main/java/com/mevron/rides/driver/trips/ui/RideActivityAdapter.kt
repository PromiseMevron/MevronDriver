package com.mevron.rides.driver.trips.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.RideActivityItemBinding
import com.mevron.rides.driver.home.data.model.home.Trip

class RideActivityAdapter() :
    ListAdapter<RideActivityDataClass, RideActivityAdapter.RideActivityHolder>(
        RideActivityDiffUti()
    ) {

    class RideActivityHolder(val binding: RideActivityItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RideActivityHolder {
        return RideActivityHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.ride_activity_item,
                parent,
                false
            )
        )
    }

    class RideActivityDiffUti : DiffUtil.ItemCallback<RideActivityDataClass>() {
        override fun areItemsTheSame(
            oldItem: RideActivityDataClass,
            newItem: RideActivityDataClass
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RideActivityDataClass,
            newItem: RideActivityDataClass
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onBindViewHolder(holder: RideActivityHolder, position: Int) {
        holder.binding.root.setOnClickListener {
        }
        val adapter = RideActivitySubAdapter()
        holder.binding.recyclerView.adapter = adapter
        adapter.submitList(getItem(position).items)
        holder.binding.date.text = getItem(position).date
        var amount = 0
        for (i in getItem(position).items) {
            amount += ((i.amount).toDoubleOrNull() ?: 0.0).toInt()
        }
        holder.binding.amount.text = amount.toString()
    }
}

data class RideActivityDataClass(val date: String = "", val items: List<Trip> =  mutableListOf())


