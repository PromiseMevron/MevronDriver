package com.mevron.rides.driver.sidemenu.supportpages.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.NotificationItemBinding
import com.mevron.rides.driver.sidemenu.NotificationSubAdapter
import com.mevron.rides.driver.sidemenu.supportpages.domain.model.Supports

class NotificationAdapter :
    ListAdapter<Supports, NotificationAdapter.NotifiHolder>(NotiDiffUtil()) {

    class NotifiHolder(val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NotiDiffUtil : DiffUtil.ItemCallback<Supports>() {
        override fun areItemsTheSame(oldItem: Supports, newItem: Supports): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Supports, newItem: Supports): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotifiHolder {
        return NotifiHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.notification_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotifiHolder, position: Int) {
        val item = getItem(position)
        holder.binding.heading.text = item.heading
        holder.binding.subheading.text = item.subHeading
    }
}