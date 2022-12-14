package com.mevron.rides.driver.sidemenu.savedplaces.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AddressItemBinding
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetSavedAddressData
import com.mevron.rides.driver.util.LocationModel

class AddressAdapter(val select: AddressSelected, val context: Context) :
    ListAdapter<GetSavedAddressData, AddressAdapter.HomeViewHolder>(AddressDiffUti()) {

    class HomeViewHolder(val binding: AddressItemBinding) : RecyclerView.ViewHolder(binding.root)

    class AddressDiffUti : DiffUtil.ItemCallback<GetSavedAddressData>() {
        override fun areItemsTheSame(
            oldItem: GetSavedAddressData,
            newItem: GetSavedAddressData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: GetSavedAddressData,
            newItem: GetSavedAddressData
        ): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.address_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val dt = getItem(position)
        if (dt.type == "home") {
            holder.binding.displayImage.setImageResource(R.drawable.ic_home_address)
            holder.binding.destType.text = context.getString(R.string.home)
        }
        if (dt.type == "work") {
            holder.binding.displayImage.setImageResource(R.drawable.ic_work_address)
            holder.binding.destType.text = context.getString(R.string.work)
        }

        if (dt.type == "others") {
            holder.binding.displayImage.setImageResource(R.drawable.ic_all_saved)
            holder.binding.destType.text = dt.name
        }
        holder.binding.destAddres.text = dt.address

        holder.binding.root.setOnClickListener {
            val data = LocationModel(
                lat = dt.lat.toDouble(),
                lng = dt.lng.toDouble(),
                address = dt.address
            )
            select.selectedAddress(data, dt)
        }
    }
}

interface AddressSelected {
    fun selectedAddress(data: LocationModel, dt: GetSavedAddressData)
}