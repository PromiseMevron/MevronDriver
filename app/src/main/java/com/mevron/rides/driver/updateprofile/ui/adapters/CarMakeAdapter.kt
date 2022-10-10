package com.mevron.rides.driver.updateprofile.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarMake

class CarMakeAdapter(val select: CarMakeSelectionListener) :
    ListAdapter<CarMake, CarMakeAdapter.CarMakeHolder>(CarMakesDiffUtil()) {

    class CarMakesDiffUtil : DiffUtil.ItemCallback<CarMake>() {
        override fun areItemsTheSame(oldItem: CarMake, newItem: CarMake): Boolean {
            return oldItem.make == newItem.make
        }

        override fun areContentsTheSame(oldItem: CarMake, newItem: CarMake): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    class CarMakeHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarMakeHolder {
        return CarMakeHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_add_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CarMakeHolder, position: Int) {
        val makes = getItem(position)
        holder.binding.locationImage.visibility = View.GONE
        holder.binding.word.text = makes.make
        holder.binding.root.setOnClickListener {
            select.onCarMakeSelected(makes.make)
        }
    }
}

interface CarMakeSelectionListener {
    fun onCarMakeSelected(car: String)
}