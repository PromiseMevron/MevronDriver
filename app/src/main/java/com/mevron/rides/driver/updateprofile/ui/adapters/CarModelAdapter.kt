package com.mevron.rides.driver.updateprofile.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardAddItemsBinding
import com.mevron.rides.driver.updateprofile.domain.model.CarModel

class CarModelAdapter(val select: CarModelSelectionListener) :
    ListAdapter<CarModel, CarModelAdapter.CarModelHolder>(CarModelDiffUtil()) {

    class CarModelHolder(val binding: CardAddItemsBinding) : RecyclerView.ViewHolder(binding.root)

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
        val models = getItem(position)
        holder.binding.word.text = models.model
        holder.binding.root.setOnClickListener {
            select.onCarModelSelected(models.model)
        }
    }

}

class CarModelDiffUtil : DiffUtil.ItemCallback<CarModel>() {
    override fun areItemsTheSame(oldItem: CarModel, newItem: CarModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CarModel, newItem: CarModel): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

interface CarModelSelectionListener {
    fun onCarModelSelected(car: String)
}