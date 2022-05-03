package com.mevron.rides.driver.updateprofile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarYearData

class CarYearAdapter(val select: CarYearSelectedListener) :
    ListAdapter<CarYearData, CarYearAdapter.CarYearHolder>(CarYearDiffUtil()) {

    class CarYearHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarYearHolder {
        return CarYearHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_add_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CarYearHolder, position: Int) {
        val makes = getItem(position)
        holder.binding.word.text = makes.year
        holder.binding.root.setOnClickListener {
            select.onYearSelected(makes.year)
        }
    }
}

class CarYearDiffUtil : DiffUtil.ItemCallback<CarYearData>() {
    override fun areItemsTheSame(oldItem: CarYearData, newItem: CarYearData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarYearData, newItem: CarYearData): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

interface CarYearSelectedListener {
    fun onYearSelected(car: String)
}