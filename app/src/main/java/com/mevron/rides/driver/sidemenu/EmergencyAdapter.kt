package com.mevron.rides.driver.sidemenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.EmergencyItemBinding
import com.mevron.rides.driver.sidemenu.model.Data

class EmergencyAdapter(val data: List<Data>, val sele: SelectedContact): RecyclerView.Adapter<EmergencyAdapter.EmergHolder>() {

    class EmergHolder(val binding: EmergencyItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergHolder {
        return EmergHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.emergency_item, parent, false))

    }

    override fun onBindViewHolder(holder: EmergHolder, position: Int) {
//[ home, work or others ]
        holder.binding.root.setOnClickListener {
            sele.selected(data[position])
        }
        holder.binding.name.text = data[position].name
        holder.binding.number.text = data[position].phoneNumber

    }

    override fun getItemCount(): Int {
        return data.size
    }
}

interface SelectedContact{
    fun selected(data: Data)
}