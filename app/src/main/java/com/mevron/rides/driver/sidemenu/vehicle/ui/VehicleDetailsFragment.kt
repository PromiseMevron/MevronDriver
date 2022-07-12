package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleDetailsFragmentBinding
import com.mevron.rides.driver.sidemenu.vehicle.ui.event.VehicleEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(), SelectVehicleDetail {

    companion object {
        fun newInstance() = VehicleDetailsFragment()
    }

    private val viewModel: VehicleViewModel by viewModels()
    private lateinit var binding: VehicleDetailsFragmentBinding
    private lateinit var adapter: VehicleDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.vehicle_details_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.onEvent(VehicleEvent.MakeAPICallDetails)
        adapter = VehicleDetailAdapter(this, requireContext())
        binding.documentRecycler.adapter = adapter
        binding.removeVehicle.setOnClickListener {
            viewModel.onEvent(VehicleEvent.DeleteVehicle)
        }
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.detail.document.isNotEmpty()) {
                        adapter =
                            VehicleDetailAdapter(this@VehicleDetailsFragment, requireContext())
                        binding.documentRecycler.adapter = adapter
                        adapter.submitList(state.detail.document)
                    }
                    if (state.isSuccess) {
                        Toast.makeText(requireContext(), "Vehicle Deleted", Toast.LENGTH_LONG)
                            .show()
                        activity?.onBackPressed()
                    }
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                    }
                    if (state.detail.image.isNotEmpty()) {
                        binding.carEnlarged.visibility = View.VISIBLE
                        Picasso.get().load(state.detail.image).placeholder(R.drawable.ic_car)
                            .error(R.drawable.ic_car).into(binding.carImage)
                        Picasso.get().load(state.detail.image).placeholder(R.drawable.ic_car)
                            .error(R.drawable.ic_car).into(binding.carEnlarged)
                    } else {
                        binding.carEnlarged.visibility = View.GONE
                    }
                    binding.carName.text = state.detail.make
                    binding.vehicleNumbers.text = state.detail.plateNumber
                    binding.color.text = state.detail.color
                    binding.year.text = state.detail.year
                    binding.carType.text = state.detail.model
                }
            }
        }
    }

    override fun selectVehicle() {

    }


}