package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleItemBinding
import com.mevron.rides.driver.sidemenu.vehicle.domain.model.AllVehicleDomainDatum
import com.squareup.picasso.Picasso

class VehicleAdapter(val sel: SelectVehicle, val useCheck: Boolean = false, val clickedPosition: Int = -1) :
    ListAdapter<AllVehicleDomainDatum, VehicleAdapter.VehiHolder>(VehicleDiffUtil()) {

    class VehiHolder(val binding: VehicleItemBinding) : RecyclerView.ViewHolder(binding.root)

    class VehicleDiffUtil : DiffUtil.ItemCallback<AllVehicleDomainDatum>() {
        override fun areItemsTheSame(
            oldItem: AllVehicleDomainDatum,
            newItem: AllVehicleDomainDatum
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: AllVehicleDomainDatum,
            newItem: AllVehicleDomainDatum
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.vehicle_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        val item = getItem(position)
        holder.binding.root.setOnClickListener {
            sel.selectVehicle(item.uuid, position)
        }
        holder.binding.carName.text = item.model
        holder.binding.carNumber.text = item.plateNumber
        if (useCheck){
            if (clickedPosition == -1)
            holder.binding.endImage.setImageResource(if (item.preference) R.drawable.ic_check else R.drawable.unchecked)
            else
                holder.binding.endImage.setImageResource(if (position == clickedPosition) R.drawable.ic_check else R.drawable.unchecked)
        }else{
            holder.binding.endImage.setImageResource(R.drawable.ic_next)
        }
        if (!item.image.isNullOrEmpty()) {
            Picasso.get().load(item.image).placeholder(R.drawable.ic_circle_car)
                .error(R.drawable.ic_circle_car).into(holder.binding.theCarImage)
        }
    }
}

interface SelectVehicle {
    fun selectVehicle(uuid: String, clickedPosition: Int = -1)
}