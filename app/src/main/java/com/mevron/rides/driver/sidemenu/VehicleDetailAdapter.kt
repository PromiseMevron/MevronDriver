package com.mevron.rides.driver.sidemenu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentItemBinding


class VehicleDetailAdapter(val sel: SelectVehicleDetail): RecyclerView.Adapter<VehicleDetailAdapter.VehiHolder>() {

    class VehiHolder(val binding: DocumentItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.document_item, parent, false))
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

interface SelectVehicleDetail{
    fun selectVehicle()
}