package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarMake

class CarMakeAdapter(val makes: List<CarMake>, val select: CarMakeSelectionListener) :
    RecyclerView.Adapter<CarMakeAdapter.CarMakeHolder>() {


    class CarMakeHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

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
        holder.binding.word.text = makes[position].make
        holder.binding.root.setOnClickListener {
            select.onCarMakeSelected(makes[position].make)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}

interface CarMakeSelectionListener {
    fun onCarMakeSelected(car: String)
}