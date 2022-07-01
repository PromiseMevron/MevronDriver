package com.mevron.rides.driver.trips.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.RideSubItemBinding
import com.mevron.rides.driver.home.data.model.home.Trip


class RideActivitySubAdapter() : ListAdapter<Trip, VehiHolder>(
    RideActivityDiffUti()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiHolder {
        return VehiHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.ride_sub_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        val data = getItem(position)
        holder.binding.amount.text = data.amount
        holder.binding.time.text = data.time
        holder.binding.location.text = ""
    }
}

class RideActivityDiffUti : DiffUtil.ItemCallback<Trip>() {
    override fun areItemsTheSame(
        oldItem: Trip,
        newItem: Trip
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: Trip,
        newItem: Trip
    ): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

class VehiHolder(val binding: RideSubItemBinding) : RecyclerView.ViewHolder(binding.root)





