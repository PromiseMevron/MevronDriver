package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleImagesFragmentBinding

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
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}