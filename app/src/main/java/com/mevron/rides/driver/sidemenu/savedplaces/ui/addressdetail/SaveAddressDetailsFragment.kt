package com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.SaveAddressDetailsFragmentBinding
import com.mevron.rides.driver.sidemenu.savedplaces.data.model.SaveAddressRequest
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail.event.SaveAddressDetailsEvent
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class SaveAddressDetailsFragment : Fragment() {

    private val viewModel: SaveAddressDetailsViewModel by viewModels()
    private lateinit var binding: SaveAddressDetailsFragmentBinding
    private var mDialog: Dialog? = null
    private lateinit var location: LocationModel
    private lateinit var data: SaveAddressRequest

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.save_address_details_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        location = SaveAddressDetailsFragmentArgs.fromBundle(
            arguments!!
        ).location
        viewModel.updateState(isLoading = false)


        viewModel.updateState(
            address = location.address,
            type = "others",
            lat = location.lat,
            lng = location.lng
        )
        binding.address.setText(location.address)

        binding.homeAddress.setOnClickListener {
            val type = viewModel.state.value.type
            if (type != "home") {
                viewModel.updateState(type = "home")
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_address_selected)
                    ?.let { it1 -> binding.homeAddress.background = it1 }
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.workAddress.background = it1 }
            }else{
                viewModel.updateState(type = "others")
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.homeAddress.background = it1 }
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.workAddress.background = it1 }
            }
        }

        binding.workAddress.setOnClickListener {
            val type = viewModel.state.value.type
            if (type != "work") {
                viewModel.updateState(type = "work")
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_address_selected)
                    ?.let { it1 -> binding.workAddress.background = it1 }
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.homeAddress.background = it1 }
            }else{
                viewModel.updateState(type = "others")
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.homeAddress.background = it1 }
                ContextCompat.getDrawable(requireContext(), R.drawable.rounded_border_save_button)
                    ?.let { it1 -> binding.workAddress.background = it1 }
            }
        }

        binding.homeAddress.setOnClickListener {
            viewModel.updateState(type = "work")
        }

        binding.backButton.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigateUp()
        }

        binding.editAddress.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
            navController.navigateUp()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    toggleBusyDialog(
                        state.isLoading,
                        desc = if (state.isLoading) "Submitting Data ...." else null
                    )

                 /*   if (state.backButton) {
                        activity?.onBackPressed()
                    }*/

                    if (state.isSuccess) {
                        Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show()
                      //  val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                     //   navController.navigateUp()
                    }

                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                        viewModel.updateState(error = "")
                    }

                    // binding.saveAddress.isEnabled = state.isCorrect

                }
            }
        }

        /*  binding.editAddress.clicks().onEach {
              viewModel.handleEvent(SaveAddressDetailsEvent.BackButtonPressed)
          }.launchIn(lifecycleScope)

          binding.backButton.clicks().onEach {
              viewModel.handleEvent(SaveAddressDetailsEvent.BackButtonPressed)
          }.launchIn(lifecycleScope)*/

        binding.saveAddress.clicks().onEach {
            viewModel.handleEvent(SaveAddressDetailsEvent.SaveAddressClicked)
        }.launchIn(lifecycleScope)

    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null) {
        if (busy) {
            if (mDialog == null) {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout, null)
                mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
            } else {
                if (!desc.isNullOrBlank()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_busy_layout, null)
                    mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
                }
            }
            mDialog?.show()
        } else {
            mDialog?.dismiss()
        }
    }

}