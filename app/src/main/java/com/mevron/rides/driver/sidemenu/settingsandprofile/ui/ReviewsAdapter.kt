package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ReviewItemsBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.data.model.Review

class ReviewsAdapter(private val context: Context) :
    ListAdapter<Review, ReviewsAdapter.ReviewsAdapterHolder>(ReviewsAdapterDifUtil()) {

    class ReviewsAdapterDifUtil : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

    }

    class ReviewsAdapterHolder(val binding: ReviewItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapterHolder {
        return ReviewsAdapterHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.review_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewsAdapterHolder, position: Int) {
        holder.binding.review.text = getItem(position).title
        holder.binding.rateDetail.text = getItem(position).message
        holder.binding.rating.rating = getItem(position).message.toFloat()
        val adapter = ReviewSubAdapter()
        holder.binding.bulletRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.bulletRecycler.adapter = adapter
        adapter.submitList(getItem(position).comment)
    }
}