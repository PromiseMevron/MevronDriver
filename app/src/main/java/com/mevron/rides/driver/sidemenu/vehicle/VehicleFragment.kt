package com.mevron.rides.driver.sidemenu.vehicle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleFragment : Fragment(), SelectVehicle {

    companion object {
        fun newInstance() = VehicleFragment()
    }

    private val viewModel: VehicleViewModel by viewModels()


    private lateinit var binding: VehicleFragmentBinding
    private lateinit var adapter: VehicleAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.vehicle_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.addAVehicle.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleFragment_to_addVehicleFragment2)
        }

        adapter = VehicleAdapter(this)
        binding.riderRecycler.adapter = adapter

        viewModel.getAllVehicle().observe(viewLifecycleOwner, Observer {

        })
    }

    override fun selectVehicle() {
        findNavController().navigate(R.id.action_vehicleFragment_to_vehicleDetailsFragment)
    }


}