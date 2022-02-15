package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.caryear.DataXXXX
import com.mevron.rides.driver.databinding.CardAddItemsBinding

class CarYearAdapter (val makes: List<DataXXXX>, val select: CarYearlselected): RecyclerView.Adapter<CarYearAdapter.CarYearHolder>() {

    class CarYearHolder(val binding: CardAddItemsBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarYearHolder {
        return CarYearHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.card_add_items, parent, false))
    }

    override fun onBindViewHolder(holder: CarYearHolder, position: Int) {
        holder.binding.word.text = makes[position].Year
        holder.binding.root.setOnClickListener {
            select.selectedYear(makes[position].Year)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}
interface CarYearlselected{
    fun selectedYear(car: String)
}