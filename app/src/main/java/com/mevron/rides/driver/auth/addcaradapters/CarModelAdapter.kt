package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.getmodel.DataXXX
import com.mevron.rides.driver.databinding.CardAddItemsBinding

class CarModelAdapter(val makes: List<DataXXX>, val select: CarModelselected): RecyclerView.Adapter<CarModelAdapter.CarModelHolder>() {




    class CarModelHolder(val binding: CardAddItemsBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarModelHolder {
        return CarModelHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.card_add_items, parent, false))
    }

    override fun onBindViewHolder(holder: CarModelHolder, position: Int) {
        holder.binding.word.text = makes[position].Model
        holder.binding.root.setOnClickListener {
            select.selectedModel(makes[position].Model)
        }
    }

    override fun getItemCount(): Int {
        return makes.size
    }
}
interface CarModelselected{
    fun selectedModel(car: String)
}