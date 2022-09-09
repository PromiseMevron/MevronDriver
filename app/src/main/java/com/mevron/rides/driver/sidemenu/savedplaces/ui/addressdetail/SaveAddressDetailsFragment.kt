package com.mevron.rides.driver.sidemenu.savedplaces.ui.addressdetail

import android.app.Dialog
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

        binding.backButton.setOnClickListener {
            Toast.makeText(requireContext(), "22222", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
        }

        binding.editAddress.setOnClickListener {
            Toast.makeText(requireContext(), "33333", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
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
                        activity?.onBackPressed()
                    }

                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, "Failure to save address", Toast.LENGTH_LONG).show()
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