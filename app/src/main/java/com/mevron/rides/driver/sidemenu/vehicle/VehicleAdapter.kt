package com.mevron.rides.driver.sidemenu.vehicle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleItemBinding

class VehicleAdapter(val sel: SelectVehicle): RecyclerView.Adapter<VehicleAdapter.VehiHolder>() {

    class VehiHolder(val binding: VehicleItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.vehicle_item, parent, false))
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
       holder.binding.root.setOnClickListener {
           sel.selectVehicle()
       }
    }

    override fun getItemCount(): Int {
        return 5
    }


}

interface SelectVehicle{
    fun selectVehicle()
}