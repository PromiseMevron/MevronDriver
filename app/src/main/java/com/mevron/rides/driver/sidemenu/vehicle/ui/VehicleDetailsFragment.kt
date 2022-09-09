package com.mevron.rides.driver.sidemenu.vehicle.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.VehicleDetailsFragmentBinding
import com.mevron.rides.driver.home.model.documents.Document
import com.mevron.rides.driver.sidemenu.vehicle.ui.event.VehicleEvent
import com.mevron.rides.driver.sidemenu.vehicle.ui.state.VehicleState
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors


@AndroidEntryPoint
class VehicleDetailsFragment : Fragment(), SelectVehicleDetail {

    companion object {
        fun newInstance() = VehicleDetailsFragment()
    }

    private val viewModel: VehicleViewModel by viewModels()
    private lateinit var binding: VehicleDetailsFragmentBinding
    private lateinit var adapter: VehicleDetailAdapter
    private var uiid: String = ""

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
        val id = arguments?.let { VehicleDetailsFragmentArgs.fromBundle(it)}?.id
        viewModel.updateState(uuid = id)
        uiid = id ?: ""
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
                    topBackgroundImage(state)
                    binding.carName.text = state.detail.make
                    binding.vehicleNumbers.text = state.detail.plateNumber
                    binding.color.text = state.detail.color
                    binding.year.text = state.detail.year
                    binding.carType.text = state.detail.model
                }
            }
        }
    }

    private fun topBackgroundImage(state: VehicleState) {
        if (!state.detail.image.isNullOrEmpty()) {
            Picasso.get().load(state.detail.image).placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car).into(binding.carImage)
            Picasso.get().load(state.detail.image).placeholder(R.drawable.ic_car)
                .error(R.drawable.ic_car).into(binding.carEnlarged)
            Executors.newSingleThreadExecutor().execute {
                try {
                    val inputStream: InputStream = URL(state.detail.image).openStream()
                    val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                    if (bitmap != null) {
                        binding.carEnlarged.visibility = View.VISIBLE
                        binding.grayBackground.visibility = View.VISIBLE
                        Palette.from(bitmap).maximumColorCount(10)
                            .generate { palette ->
                                // Get the "vibrant" color swatch based on the bitmap
                                val vibrant: Palette.Swatch? = palette?.vibrantSwatch
                                if (vibrant != null) {
                                    binding.grayBackground.setBackgroundColor(vibrant.rgb)
                                    binding.grayBackground.alpha = 0.7F
                                    binding.carName.setTextColor(vibrant.titleTextColor)
                                    binding.carType.setTextColor(vibrant.titleTextColor)
                                }
                            }
                    }
                } catch (e: Exception) {
                    print(e.message)
                }
            }
        } else {
            binding.grayBackground.visibility = View.GONE
            binding.carEnlarged.visibility = View.GONE
        }
    }

    override fun selectVehicle(document: Document) {
        if (document.name == "Driver License" && document.status == 0){
            val action = VehicleDetailsFragmentDirections.actionGlobalDocumentFragment(true)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Registration Sticker" && document.status == 0){
            val action = VehicleDetailsFragmentDirections.actionGlobalStickerFragment(true, uiid)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Insurance" && document.status == 0){
            val action = VehicleDetailsFragmentDirections.actionGlobalInsuranceFragment(true, uiid)
            findNavController().navigate(action)
        }
        if (document.name == "Profile Photo" && document.status == 0){
            findNavController().navigate(R.id.action_global_profileFragment)
        }
        if (document.name == "Driver License" && document.status == 2){
            val action = VehicleDetailsFragmentDirections.actionGlobalDocumentFragment(true)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Registration Sticker" && document.status == 2){
            val action = VehicleDetailsFragmentDirections.actionGlobalStickerFragment(true, uiid)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Insurance" && document.status == 2){
            val action = VehicleDetailsFragmentDirections.actionGlobalInsuranceFragment(true, uiid)
            findNavController().navigate(action)
        }
        if (document.name == "Profile Photo" && document.status == 2){
            findNavController().navigate(R.id.action_global_profileFragment)
        }
        if (document.status != 0 && document.status != 2){
                val action = VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToVehicleImagesFragment(document.url, document.name)
                findNavController().navigate(action)
        }
    }


}