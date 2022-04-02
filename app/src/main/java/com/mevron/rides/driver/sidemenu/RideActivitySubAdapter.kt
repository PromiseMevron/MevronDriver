package com.mevron.rides.driver.sidemenu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentItemBinding
import com.mevron.rides.driver.databinding.RideSubActivityBinding

class RideActivitySubAdapter(val context: Context): RecyclerView.Adapter<RideActivitySubAdapter.VehiHolder>() {

    class VehiHolder(val binding: RideSubActivityBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VehiHolder {
        return VehiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.ride_sub_activity, parent, false))
    }

    override fun onBindViewHolder(holder: VehiHolder, position: Int) {
        holder.binding.root.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return 3
    }


}

