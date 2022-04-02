package com.mevron.rides.driver.sidemenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R

import com.mevron.rides.driver.databinding.RideActivityItemBinding

class RideActivityAdapter(): RecyclerView.Adapter<RideActivityAdapter.VehiHolder>() {

    class VehiHolder(val binding: RideActivityItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.ride_activity_item, parent, false))
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        holder.binding.root.setOnClickListener {

        }

        holder.binding.recyclerView.adapter = RideActivitySubAdapter(holder.binding.recyclerView.context)
    }

    override fun getItemCount(): Int {
        return 7
    }


}

