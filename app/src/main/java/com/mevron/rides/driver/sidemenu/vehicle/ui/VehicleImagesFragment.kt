package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleImagesFragmentBinding
import com.squareup.picasso.Picasso

class VehicleImagesFragment : Fragment() {

    companion object {
        fun newInstance() = VehicleImagesFragment()
    }

    private lateinit var binding: VehicleImagesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.vehicle_images_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = VehicleImagesFragmentArgs.fromBundle(requireArguments()).url
        val name = VehicleImagesFragmentArgs.fromBundle(requireArguments()).name
        binding.text4.text = name
        if (!url.isNullOrEmpty()){
            Picasso.get().load(url).into(binding.image)
        }
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}