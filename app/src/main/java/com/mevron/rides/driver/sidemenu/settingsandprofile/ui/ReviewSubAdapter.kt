package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.BulletItemBinding

class ReviewSubAdapter :
    ListAdapter<String, ReviewSubAdapter.ReviewSubHolder>(ReviewSubDiffUtil()) {

    class ReviewSubDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

    }

    class ReviewSubHolder(val binding: BulletItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewSubHolder {
        return ReviewSubHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.bullet_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewSubHolder, position: Int) {
        holder.binding.rateDetail.text = getItem(position)
    }
}