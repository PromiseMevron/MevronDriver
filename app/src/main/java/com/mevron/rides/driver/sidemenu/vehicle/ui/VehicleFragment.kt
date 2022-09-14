package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleFragmentBinding
import com.mevron.rides.driver.sidemenu.vehicle.ui.event.VehicleEvent
import com.mevron.rides.driver.updateprofile.ui.Register1FragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
        viewModel.updateState(isLoading = false)
        viewModel.onEvent(VehicleEvent.MakeAPICall)
        binding.addAVehicle.setOnClickListener {
           // val action = VehicleFragmentDirections.actionVehicleFragmentToAddVehicleFragment2(true)
           // findNavController().navigate(action)
            findNavController().navigate(R.id.action_vehicleFragment_to_addNewVehicleFragment)
           // findNavController().navigate(R.id.action_vehicleFragment_to_addVehicleFragment2)
        }
        adapter = VehicleAdapter(this)
        binding.riderRecycler.adapter = adapter

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.vehicle.isNotEmpty()){
                        adapter = VehicleAdapter(this@VehicleFragment)
                        binding.riderRecycler.adapter = adapter
                        adapter.submitList(state.vehicle)
                    }
                }
            }
        }
    }

    override fun selectVehicle(uuid: String, clickedPosition: Int) {
        viewModel.updateState(uuid = uuid)
        val action = VehicleFragmentDirections.actionVehicleFragmentToVehicleDetailsFragment(uuid)
        findNavController().navigate(action)
    }


}