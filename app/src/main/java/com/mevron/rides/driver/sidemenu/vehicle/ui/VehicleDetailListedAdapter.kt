package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentVehicleItemBinding
import com.mevron.rides.driver.documentcheck.data.model.CarProperty

class VehicleDetailListedAdapter(val sel: SelectVehicleDetailDocument, val context: Context) :
    ListAdapter<CarProperty, VehicleDetailListedAdapter.VehiHolder>(
        VehicleDetailDiffUtil()
    ) {

    class VehicleDetailDiffUtil : DiffUtil.ItemCallback<CarProperty>() {
        override fun areItemsTheSame(oldItem: CarProperty, newItem: CarProperty): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CarProperty, newItem: CarProperty): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    class VehiHolder(val binding: DocumentVehicleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.document_vehicle_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        val doc = getItem(position)
       holder.binding.carProp.text = "${doc.make} - ${doc.plateNumber}"
        val adapter = VehicleDetailDocumentAdapter(sel, context, doc.id)
        holder.binding.recyclerView.adapter = adapter
        adapter.submitList(doc.document)
    }


}
