package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarYearData

class CarYearAdapter(val makes: List<CarYearData>, val select: CarYearSelectedListener) :
    RecyclerView.Adapter<CarYearAdapter.CarYearHolder>() {

    class CarYearHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root) {

    }

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
        holder.binding.word.text = makes[position].year
        holder.binding.root.setOnClickListener {
            select.onYearSelected(makes[position].year)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}

interface CarYearSelectedListener {
    fun onYearSelected(car: String)
}