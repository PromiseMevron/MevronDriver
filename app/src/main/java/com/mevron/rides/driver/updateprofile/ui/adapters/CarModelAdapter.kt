package com.mevron.rides.driver.updateprofile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarModel

class CarModelAdapter(val makes: List<CarModel>, val select: CarModelSelectionListener) :
    RecyclerView.Adapter<CarModelAdapter.CarModelHolder>() {


    class CarModelHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelHolder {
        return CarModelHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.card_add_items,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CarModelHolder, position: Int) {
        holder.binding.word.text = makes[position].model
        holder.binding.root.setOnClickListener {
            select.onCarModelSelected(makes[position].model)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}

interface CarModelSelectionListener {
    fun onCarModelSelected(car: String)
}