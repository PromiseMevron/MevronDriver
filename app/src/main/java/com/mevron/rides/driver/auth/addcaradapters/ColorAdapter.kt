package com.mevron.rides.driver.auth.addcaradapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ColorItemBinding

class ColorAdapter(val select: ColorSelectionListener): RecyclerView.Adapter<ColorAdapter.ColorHolder>() {

    var names = arrayListOf("Black", "Grey", "Silver", "Blue", "Brown", "Gold", "White", "Other")
    var images = arrayListOf<Int>(R.drawable.black, R.drawable.grey, R.drawable.silver, R.drawable.blue, R.drawable.brown, R.drawable.gold, R.drawable.white, R.drawable.other)
    class ColorHolder(val binding: ColorItemBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
        return ColorHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.color_item, parent, false))
    }

    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
        holder.binding.word.text = names[position]
        holder.binding.color.setImageResource(images[position])
        holder.binding.root.setOnClickListener {
            select.onColorSelected(names[position])
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }
}

interface ColorSelectionListener{
    fun onColorSelected(color: String)
}