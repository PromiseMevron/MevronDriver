package com.mevron.rides.driver.sidemenu.vehicle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(), SelectVehicleDetail {

    companion object {
        fun newInstance() = VehicleDetailsFragment()
    }

    private val viewModel: VehicleDetailsViewModel by viewModels()
    private lateinit var binding: VehicleDetailsFragmentBinding
    private lateinit var adapter: VehicleDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.vehicle_details_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        adapter = VehicleDetailAdapter(this, requireContext())
        binding.documentRecycler.adapter = adapter
        viewModel.getAVehicle("").observe(viewLifecycleOwner, Observer {

        })
    }

    override fun selectVehicle() {
       // findNavController().navigate(R.id.action_vehicleDetailsFragment_to_vehicleImagesFragment)
    }


}