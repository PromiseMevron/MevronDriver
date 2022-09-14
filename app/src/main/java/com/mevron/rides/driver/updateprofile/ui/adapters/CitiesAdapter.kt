package com.mevron.rides.driver.updateprofile.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.authentication.domain.model.GetCitiesData
import com.mevron.rides.driver.databinding.CardAddItemsBinding

class CitiesAdapter(val select: CitySelectedListener) :
    ListAdapter<GetCitiesData, CitiesAdapter.CityHolder>(CitiesDiffUtil()) {

    class CityHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        return CityHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_add_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        val makes = getItem(position)
        holder.binding.word.text = makes.name
        holder.binding.locationImage.visibility = View.VISIBLE
        holder.binding.root.setOnClickListener {
            select.onCitySelected(makes)
        }
    }
}

class CitiesDiffUtil : DiffUtil.ItemCallback<GetCitiesData>() {
    override fun areItemsTheSame(oldItem: GetCitiesData, newItem: GetCitiesData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GetCitiesData, newItem: GetCitiesData): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

interface CitySelectedListener {
    fun onCitySelected(city: GetCitiesData)
}