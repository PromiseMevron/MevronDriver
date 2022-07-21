package com.mevron.rides.driver.home.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ScheduleItemBinding
import com.mevron.rides.driver.home.data.model.home.ScheduledPickup

class ScheduleAdapter :
    ListAdapter<ScheduledPickup, ScheduleAdapter.ScheduleHolder>(ScheduleDiffUtil()) {
    class ScheduleDiffUtil : DiffUtil.ItemCallback<ScheduledPickup>() {
        override fun areItemsTheSame(oldItem: ScheduledPickup, newItem: ScheduledPickup): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScheduledPickup,
            newItem: ScheduledPickup
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

    }

    class ScheduleHolder(val binding: ScheduleItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHolder {
        return ScheduleHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    parent.context
                ), R.layout.schedule_item, parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {
        val item = getItem(position)
        holder.binding.address.text = item.pickupAddress
        holder.binding.time.text = item.schedulePickupTime
        holder.binding.amount.text = "${item.minEstimatedCost} - ${item.maxEstimatedCost}"
    }
}