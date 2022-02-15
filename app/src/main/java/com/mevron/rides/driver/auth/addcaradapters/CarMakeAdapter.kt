package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.getcar.DataXX
import com.mevron.rides.driver.databinding.CardAddItemsBinding

class CarMakeAdapter(val makes: List<DataXX>, val select: Carselected): RecyclerView.Adapter<CarMakeAdapter.CarMakeHolder>() {




    class CarMakeHolder(val binding: CardAddItemsBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarMakeHolder {
        return CarMakeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.card_add_items, parent, false))
    }

    override fun onBindViewHolder(holder: CarMakeHolder, position: Int) {
        holder.binding.word.text = makes[position].Make
        holder.binding.root.setOnClickListener {
            select.selectedCar(makes[position].Make)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}
interface Carselected{
    fun selectedCar(car: String)
}