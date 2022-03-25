package com.mevron.rides.driver.sidemenu.savedplaces

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AddressItemBinding
import com.mevron.rides.driver.localdb.SavedAddress
import com.mevron.rides.driver.util.LocationModel

class AddressAdapter(val data: List<SavedAddress>, val select: AddressSelected): RecyclerView.Adapter<AddressAdapter.HomeViewHolder>() {

    class HomeViewHolder(val binding: AddressItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.address_item, parent, false))

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
//[ home, work or others ]
        val dt = data[position]
        if (dt.type == "home"){
            holder.binding.displayImage.setImageResource(R.drawable.ic_home_address)
            holder.binding.destType.text = "Home"
        }
        if (dt.type == "work"){
            holder.binding.displayImage.setImageResource(R.drawable.ic_work_address)
            holder.binding.destType.text = "Work"
        }

        if (dt.type == "others"){
            holder.binding.displayImage.setImageResource(R.drawable.ic_all_saved)
            holder.binding.destType.text = dt.name
        }

        holder.binding.destAddres.text = dt.address

        holder.binding.root.setOnClickListener {
            val data = LocationModel(lat = dt.lat.toDouble(), lng = dt.lng.toDouble(), address = dt.address)
            select.selectedAddress(data, dt)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}

interface AddressSelected{
    fun selectedAddress(data: LocationModel, dt: SavedAddress)
}